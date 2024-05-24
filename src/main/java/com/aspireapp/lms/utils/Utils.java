package com.aspireapp.lms.utils;

import com.aspireapp.lms.model.domain.LoanApplication;
import com.aspireapp.lms.model.domain.LoanRepayment;
import com.aspireapp.lms.model.domain.LoanApplicationStatus;
import com.aspireapp.lms.model.request.CreateLoanApplicationRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class Utils {

    private LocalDate getCurrentUtcDateTime() {
        return LocalDate.now();
    }

    /**
     * Helper function to create LoanApplication with repayments
     */
    public LoanApplication createLoanApplication(CreateLoanApplicationRequest request) {
        LoanApplication loanApplication = LoanApplication.builder()
                .id(UUID.randomUUID())
                .userId(request.getUserId())
                .amount(request.getAmountRequired())
                .termInWeeks(request.getLoanTerm())
                .status(LoanApplicationStatus.PENDING)
                .createdAt(getCurrentUtcDateTime())
                .build();

        List<LoanRepayment> repaymentList = createRepaymentsForLoanApplication(loanApplication);
        loanApplication.setRepayments(repaymentList);
        return loanApplication;
    }

    /**
     * Helper function to create repayments for LoanApplication
     */
    private List<LoanRepayment> createRepaymentsForLoanApplication(LoanApplication loanApplication) {
        List<LoanRepayment> repayments = new ArrayList<>();
        Double repaymentAmount = loanApplication.getAmount() / loanApplication.getTermInWeeks();
        LocalDate currentDate = loanApplication.getCreatedAt();
        for (Integer repayment = 1; repayment <= loanApplication.getTermInWeeks(); repayment++) {
            LocalDate dueDate = getNextDueDate(currentDate);
            repayments.add(new LoanRepayment(loanApplication.getId(), repaymentAmount, dueDate));
            currentDate = dueDate;
        }
        return repayments;
    }

    /**
     * Helper function to get dueDate for next repayment
     */
    private LocalDate getNextDueDate(LocalDate date) {
        return date.plusDays(7);
    }
}
