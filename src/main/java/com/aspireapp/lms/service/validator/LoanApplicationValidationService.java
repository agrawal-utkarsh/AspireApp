package com.aspireapp.lms.service.validator;

import com.aspireapp.lms.exception.ValidationException;
import com.aspireapp.lms.model.domain.LoanApplication;
import com.aspireapp.lms.model.domain.LoanApplicationStatus;
import com.aspireapp.lms.model.request.CreateLoanApplicationRequest;
import com.aspireapp.lms.model.request.SubmitLoanRepaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.aspireapp.lms.model.domain.Constants.*;

@Service
@Slf4j
public class LoanApplicationValidationService {

    public void validateLoanApplicationCreationRequest(CreateLoanApplicationRequest request) throws ValidationException {
        if (request.getAmountRequired() <= 0) {
            throw new ValidationException(INCORRECT_LOAN_AMOUNT_MESSAGE);
        }
        if (request.getLoanTerm() <= 0) {
            throw new ValidationException(INCORRECT_LOAN_TENURE_MESSAGE);
        }
    }

    public void validateLoanApplicationApprovalRequest(Optional<LoanApplication> loanApplicationOption) throws ValidationException {
        if (loanApplicationOption.isEmpty()) {
            throw new ValidationException(LOAN_NOT_FOUND_MESSAGE);
        }
        if(loanApplicationOption.get().getStatus().equals(LoanApplicationStatus.APPROVED)){
            throw new ValidationException(LOAN_ALREADY_APPROVED_MESSAGE);
        }
        if(loanApplicationOption.get().getStatus().equals(LoanApplicationStatus.PAID)){
            throw new ValidationException(LOAN_ALREADY_PAID_MESSAGE);
        }
        if(loanApplicationOption.get().getStatus().equals(LoanApplicationStatus.DENIED)){
            throw new ValidationException(LOAN_ALREADY_DENIED_MESSAGE);
        }
    }

    public void validateLoanRepaymentRequest(SubmitLoanRepaymentRequest request, Optional<LoanApplication> loanApplicationOption) throws ValidationException {
        if (request.getAmount() <= 0) {
            throw new ValidationException(INCORRECT_LOAN_REPAYMENT_AMOUNT_MESSAGE);
        }
        if (loanApplicationOption.isEmpty()) {
            throw new ValidationException(LOAN_NOT_FOUND_MESSAGE);
        }
        if (loanApplicationOption.get().getStatus().equals(LoanApplicationStatus.PENDING)) {
            throw new ValidationException(LOAN_PENDING_APPROVAL_MESSAGE);
        }
        if (loanApplicationOption.get().getStatus().equals(LoanApplicationStatus.PAID)) {
            throw new ValidationException(LOAN_ALREADY_PAID_MESSAGE);
        }
        if (loanApplicationOption.get().getStatus().equals(LoanApplicationStatus.DENIED)) {
            throw new ValidationException(LOAN_ALREADY_DENIED_MESSAGE);
        }
    }
}
