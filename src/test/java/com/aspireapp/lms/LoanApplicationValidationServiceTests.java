package com.aspireapp.lms;

import com.aspireapp.lms.model.domain.LoanApplication;
import com.aspireapp.lms.model.domain.LoanApplicationStatus;
import com.aspireapp.lms.model.request.CreateLoanApplicationRequest;
import com.aspireapp.lms.model.request.SubmitLoanRepaymentRequest;
import com.aspireapp.lms.service.validator.LoanApplicationValidationService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static com.aspireapp.lms.model.domain.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
class LoanApplicationValidationServiceTests {

    @Autowired
    private LoanApplicationValidationService service;

    private final UUID userId = UUID.randomUUID();
    private final UUID loanId = UUID.randomUUID();
    private LoanApplication loanApplication = LoanApplication.builder().build();

    @Test
    void createLoanApplication_ZeroAmountTest() {
        CreateLoanApplicationRequest request = new CreateLoanApplicationRequest(userId, 0D, 1);
        try {
            service.validateLoanApplicationCreationRequest(request);
        } catch (Exception exception) {
            assertEquals(INCORRECT_LOAN_AMOUNT_MESSAGE, exception.getMessage());
        }
    }

    @Test
    void createLoanApplication_NegativeAmountTest() {
        CreateLoanApplicationRequest request = new CreateLoanApplicationRequest(userId, -10D, 1);
        try {
            service.validateLoanApplicationCreationRequest(request);
        } catch (Exception exception) {
            assertEquals(INCORRECT_LOAN_AMOUNT_MESSAGE, exception.getMessage());
        }
    }

    @Test
    void createLoanApplication_ZeroTenureTest() {
        CreateLoanApplicationRequest request = new CreateLoanApplicationRequest(userId, 10D, 0);
        try {
            service.validateLoanApplicationCreationRequest(request);
        } catch (Exception exception) {
            assertEquals(INCORRECT_LOAN_TENURE_MESSAGE, exception.getMessage());
        }
    }

    @Test
    void createLoanApplication_NegativeTenureTest() {
        CreateLoanApplicationRequest request = new CreateLoanApplicationRequest(userId, 10D, -1);
        try {
            service.validateLoanApplicationCreationRequest(request);
        } catch (Exception exception) {
            assertEquals(INCORRECT_LOAN_TENURE_MESSAGE, exception.getMessage());
        }
    }

    @Test
    void approveLoanApplication_IncorrectLoanIdTest() {
        try {
            service.validateLoanApplicationApprovalRequest(Optional.empty());
        } catch (Exception exception) {
            assertEquals(LOAN_NOT_FOUND_MESSAGE, exception.getMessage());
        }
    }

    @Test
    void approveLoanApplication_LoanAlreadyApprovedTest() {
        loanApplication.setStatus(LoanApplicationStatus.APPROVED);
        try {
            service.validateLoanApplicationApprovalRequest(Optional.of(loanApplication));
        } catch (Exception exception) {
            assertEquals(LOAN_ALREADY_APPROVED_MESSAGE, exception.getMessage());
        }
    }

    @Test
    void approveLoanApplication_LoanAlreadyPaidTest() {
        loanApplication.setStatus(LoanApplicationStatus.PAID);
        try {
            service.validateLoanApplicationApprovalRequest(Optional.of(loanApplication));
        } catch (Exception exception) {
            assertEquals(LOAN_ALREADY_PAID_MESSAGE, exception.getMessage());
        }
    }

    @Test
    void approveLoanApplication_LoanAlreadyDeniedTest() {
        loanApplication.setStatus(LoanApplicationStatus.DENIED);
        try {
            service.validateLoanApplicationApprovalRequest(Optional.of(loanApplication));
        } catch (Exception exception) {
            assertEquals(LOAN_ALREADY_DENIED_MESSAGE, exception.getMessage());
        }
    }

    @Test
    void submitLoanRepayment_IncorrectAmountRepaidTest() {
        SubmitLoanRepaymentRequest request = new SubmitLoanRepaymentRequest(0D);
        try {
            service.validateLoanRepaymentRequest(request, Optional.of(loanApplication));
        } catch (Exception exception) {
            assertEquals(INCORRECT_LOAN_REPAYMENT_AMOUNT_MESSAGE, exception.getMessage());
        }
    }

    @Test
    void submitLoanRepayment_LoanNotFoundTest() {
        SubmitLoanRepaymentRequest request = new SubmitLoanRepaymentRequest(10D);
        try {
            service.validateLoanRepaymentRequest(request, Optional.empty());
        } catch (Exception exception) {
            assertEquals(LOAN_NOT_FOUND_MESSAGE, exception.getMessage());
        }
    }

    @Test
    void submitLoanRepayment_LoanPendingTest() {
        SubmitLoanRepaymentRequest request = new SubmitLoanRepaymentRequest(10D);
        loanApplication.setStatus(LoanApplicationStatus.PENDING);
        try {
            service.validateLoanRepaymentRequest(request, Optional.of(loanApplication));
        } catch (Exception exception) {
            assertEquals(LOAN_PENDING_APPROVAL_MESSAGE, exception.getMessage());
        }
    }

    @Test
    void submitLoanRepayment_LoanAlreadyPaidTest() {
        SubmitLoanRepaymentRequest request = new SubmitLoanRepaymentRequest(10D);
        loanApplication.setStatus(LoanApplicationStatus.PAID);
        try {
            service.validateLoanRepaymentRequest(request, Optional.of(loanApplication));
        } catch (Exception exception) {
            assertEquals(LOAN_ALREADY_PAID_MESSAGE, exception.getMessage());
        }
    }

    @Test
    void submitLoanRepayment_LoanAlreadyDeniedTest() {
        SubmitLoanRepaymentRequest request = new SubmitLoanRepaymentRequest(10D);
        loanApplication.setStatus(LoanApplicationStatus.DENIED);
        try {
            service.validateLoanRepaymentRequest(request, Optional.of(loanApplication));
        } catch (Exception exception) {
            assertEquals(LOAN_ALREADY_DENIED_MESSAGE, exception.getMessage());
        }
    }

}
