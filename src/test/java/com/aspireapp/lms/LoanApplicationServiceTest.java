package com.aspireapp.lms;

import com.aspireapp.lms.exception.ValidationException;
import com.aspireapp.lms.model.domain.LoanApplication;
import com.aspireapp.lms.model.domain.LoanApplicationStatus;
import com.aspireapp.lms.model.domain.LoanRepayment;
import com.aspireapp.lms.model.domain.RepaymentStatus;
import com.aspireapp.lms.model.request.CreateLoanApplicationRequest;
import com.aspireapp.lms.model.request.SubmitLoanRepaymentRequest;
import com.aspireapp.lms.repository.LoanApplicationRepository;
import com.aspireapp.lms.repository.LoanRepaymentRepository;
import com.aspireapp.lms.service.LoanApplicationService;
import com.aspireapp.lms.service.validator.LoanApplicationValidationService;
import com.aspireapp.lms.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LoanApplicationServiceTest {

    @InjectMocks
    private LoanApplicationService loanApplicationService;

    @Mock
    private LoanApplicationRepository loanApplicationRepository;

    @Mock
    private LoanRepaymentRepository loanRepaymentRepository;

    @Mock
    private LoanApplicationValidationService validationService;

    @Mock
    private Utils utils;

    private CreateLoanApplicationRequest createLoanApplicationRequest;
    private SubmitLoanRepaymentRequest submitLoanRepaymentRequest;
    private LoanApplication loanApplication;
    private LoanRepayment loanRepayment;

    @BeforeEach
    public void setUp() {
        createLoanApplicationRequest = new CreateLoanApplicationRequest();
        createLoanApplicationRequest.setAmountRequired(1000.0);
        createLoanApplicationRequest.setUserId(UUID.randomUUID());

        submitLoanRepaymentRequest = new SubmitLoanRepaymentRequest();
        submitLoanRepaymentRequest.setAmount(100.0);

        loanRepayment = new LoanRepayment();
        loanRepayment.setAmount(100.0);
        loanRepayment.setStatus(RepaymentStatus.PENDING);

        loanApplication = new LoanApplication();
        loanApplication.setId(UUID.randomUUID());
        loanApplication.setAmount(1000.0);
        loanApplication.setStatus(LoanApplicationStatus.PENDING);
        loanApplication.setRepayments(Arrays.asList(loanRepayment));
    }

    @Test
    public void testCreateLoanApplication() throws ValidationException {
        when(utils.createLoanApplication(any(CreateLoanApplicationRequest.class))).thenReturn(loanApplication);

        loanApplicationService.createLoanApplication(createLoanApplicationRequest);

        verify(validationService).validateLoanApplicationCreationRequest(createLoanApplicationRequest);
        verify(loanRepaymentRepository).saveAll(anyList());
        verify(loanApplicationRepository).save(loanApplication);
    }

    @Test
    public void testApproveLoan() throws ValidationException {
        when(loanApplicationRepository.findById(any(UUID.class))).thenReturn(Optional.of(loanApplication));

        loanApplicationService.approveLoan(loanApplication.getId());

        assertEquals(LoanApplicationStatus.APPROVED, loanApplication.getStatus());
        verify(validationService).validateLoanApplicationApprovalRequest(any(Optional.class));
        verify(loanApplicationRepository).save(loanApplication);
    }

    @Test
    public void testGetLoanApplicationsForUser() {
        UUID userId = UUID.randomUUID();
        List<LoanApplication> loanApplications = Arrays.asList(loanApplication);
        when(loanApplicationRepository.findAllByUserId(userId)).thenReturn(loanApplications);

        List<LoanApplication> result = loanApplicationService.getLoanApplicationsForUser(userId);

        assertEquals(loanApplications, result);
        verify(loanApplicationRepository).findAllByUserId(userId);
    }

    @Test
    public void testSubmitLoanRepayment() throws ValidationException {
        when(loanApplicationRepository.findById(any(UUID.class))).thenReturn(Optional.of(loanApplication));

        loanApplicationService.submitLoanRepayment(loanApplication.getId(), submitLoanRepaymentRequest);

        verify(validationService).validateLoanRepaymentRequest(submitLoanRepaymentRequest, Optional.of(loanApplication));
        verify(loanRepaymentRepository).saveAll(anyList());
        verify(loanApplicationRepository).save(loanApplication);
    }

    @Test
    public void testSkipPaidRepayments() throws ValidationException {
        List<LoanRepayment> repayments = new ArrayList<>();
        LoanRepayment paidRepayment = new LoanRepayment();
        paidRepayment.setAmount(200.0);
        paidRepayment.setStatus(RepaymentStatus.PAID);
        repayments.add(paidRepayment);

        LoanRepayment unpaidRepayment = new LoanRepayment();
        unpaidRepayment.setAmount(300.0);
        unpaidRepayment.setStatus(RepaymentStatus.PENDING);
        repayments.add(unpaidRepayment);

        int index = loanApplicationService.skipPaidRepayments(repayments, 300.0, 500.0);

        assertEquals(1, index);
    }

    @Test
    public void testUpdateRepayments() {
        List<LoanRepayment> repayments = new ArrayList<>();
        LoanRepayment repayment1 = new LoanRepayment();
        repayment1.setAmount(200.0);
        repayment1.setStatus(RepaymentStatus.PAID);

        LoanRepayment repayment2 = new LoanRepayment();
        repayment2.setAmount(100.0);
        repayment2.setStatus(RepaymentStatus.PAID);

        repayments.add(repayment1);
        repayments.add(repayment2);

        int remaining = loanApplicationService.updateRepayments(250.0, repayments, 0);

        assertEquals(1, remaining);
        assertEquals(RepaymentStatus.PAID, repayments.get(0).getStatus());
        assertEquals(RepaymentStatus.PAID, repayments.get(1).getStatus());
        assertEquals(50.0, repayments.get(1).getAmount());
    }
}
