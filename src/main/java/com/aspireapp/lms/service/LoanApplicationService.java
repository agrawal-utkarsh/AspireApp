package com.aspireapp.lms.service;

import com.aspireapp.lms.exception.ValidationException;
import com.aspireapp.lms.model.domain.LoanApplicationStatus;
import com.aspireapp.lms.model.domain.LoanRepayment;
import com.aspireapp.lms.model.domain.RepaymentStatus;
import com.aspireapp.lms.model.request.ApproveLoanRequest;
import com.aspireapp.lms.model.request.CreateLoanApplicationRequest;
import com.aspireapp.lms.model.request.GetLoanApplicationsForUserRequest;
import com.aspireapp.lms.model.request.SubmitLoanRepaymentRequest;
import com.aspireapp.lms.model.domain.LoanApplication;
import com.aspireapp.lms.repository.LoanApplicationRepository;
import com.aspireapp.lms.repository.LoanRepaymentRepository;
import com.aspireapp.lms.service.validator.LoanApplicationValidationService;
import com.aspireapp.lms.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class LoanApplicationService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;
    @Autowired
    private LoanRepaymentRepository loanRepaymentRepository;
    @Autowired
    private LoanApplicationValidationService validationService;
    @Autowired
    private Utils utils;

    /**
     * Validates incoming request
     * Creates LoanApplication with repayments
     * Saves LoanApplication and repayments to database
     */
    public void createLoanApplication(CreateLoanApplicationRequest request) throws ValidationException {
        validationService.validateLoanApplicationCreationRequest(request);

        LoanApplication loanApplication = utils.createLoanApplication(request);
        loanRepaymentRepository.saveAll(loanApplication.getRepayments());
        loanApplicationRepository.save(loanApplication);
    }

    /**
     * Validates incoming request
     * Updates LoanApplication status
     * Saves updated LoanApplication to database
     */
    public void approveLoan(ApproveLoanRequest request) throws ValidationException {
        Optional<LoanApplication> loanApplicationOption = loanApplicationRepository.findById(request.getLoanId());
        validationService.validateLoanApplicationApprovalRequest(loanApplicationOption);

        LoanApplication loanApplication = loanApplicationOption.get();
        loanApplication.setStatus(LoanApplicationStatus.APPROVED);
        loanApplicationRepository.save(loanApplication);
    }

    /**
     * Fetches LoanApplications for incoming user from database
     */
    public List<LoanApplication> getLoanApplicationsForUser(GetLoanApplicationsForUserRequest request) {
        List<LoanApplication> loanApplications = loanApplicationRepository.findAllByUserId(request.getUserId());
        return loanApplications;
    }

    /**
     * Validates incoming request
     * Skips through already paid repayments
     * Updates pending repayments and loanApplication (if applicable)
     * Saves updated LoanApplication and repayments to database
     */
    public void submitLoanRepayment(SubmitLoanRepaymentRequest request) throws ValidationException {
        Optional<LoanApplication> loanApplicationOption = loanApplicationRepository.findById(request.getLoanApplicationId());
        validationService.validateLoanRepaymentRequest(request, loanApplicationOption);

        LoanApplication loanApplication = loanApplicationOption.get();
        List<LoanRepayment> repayments = loanApplication.getRepayments();

        Integer currentRepaymentIndex = skipPaidRepayments(repayments, request.getAmount(), loanApplication.getAmount());
        Integer repaymentsRemaining = updateRepayments(request.getAmount(), repayments, currentRepaymentIndex);

        if (repaymentsRemaining == 0) {
            loanApplication.setStatus(LoanApplicationStatus.PAID);
            loanApplicationRepository.save(loanApplication);
        }
    }

    /**
     * Iterates through already paid repayments
     * Executes validation checks for the amount in incoming request and remainingAmount
     * Returns the index of first unpaid repayment
     */
    private Integer skipPaidRepayments(List<LoanRepayment> repayments, Double repaymentAmount, Double loanApplicationAmount) throws ValidationException {
        Integer currentRepaymentIndex = 0;
        Double amountAlreadyPaid = 0D;

        while (repayments.get(currentRepaymentIndex).getStatus().equals(RepaymentStatus.PAID)) {
            amountAlreadyPaid += repayments.get(currentRepaymentIndex).getAmount();
            currentRepaymentIndex++;
        }

        Double remainingAmount = loanApplicationAmount - amountAlreadyPaid;
        if (repaymentAmount > remainingAmount) {
            throw new ValidationException("Repayment amount is greater than pending amount. Repayment cannot be done.");
        }
        return currentRepaymentIndex;
    }

    /**
     * Updates unpaid repayments to PAID status and also updates their remaining amount
     * Saves the updated repayments to database
     */
    private Integer updateRepayments(Double repaymentAmount, List<LoanRepayment> repayments, Integer currentRepaymentIndex) {
        List<LoanRepayment> repaymentsToBeUpdated = new ArrayList<>();
        Integer repaymentsRemaining = repayments.size() - currentRepaymentIndex;
        while (repaymentAmount > 0 && currentRepaymentIndex < repayments.size()) {
            LoanRepayment repaymentToBeUpdated = repayments.get(currentRepaymentIndex);
            Double repaymentToBeUpdatedAmount = repaymentToBeUpdated.getAmount();
            if (repaymentAmount >= repaymentToBeUpdatedAmount) {
                repaymentsRemaining--;
                repaymentToBeUpdated.setStatus(RepaymentStatus.PAID);
            }
            repaymentToBeUpdated.setAmount(Math.max(0, repaymentToBeUpdatedAmount - repaymentAmount));
            repaymentAmount = Math.max(0, repaymentAmount - repaymentToBeUpdatedAmount);
            repaymentsToBeUpdated.add(repaymentToBeUpdated);
            currentRepaymentIndex++;
        }
        loanRepaymentRepository.saveAll(repaymentsToBeUpdated);
        return repaymentsRemaining;
    }
}
