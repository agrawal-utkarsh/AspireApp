package com.aspireapp.lms.model.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Builder
@AllArgsConstructor
public class LoanApplication {

    @Id
    private UUID id;
    private UUID userId;
    private Double amount;
    private Integer termInWeeks;
    private Double interestRate;
    private LoanApplicationStatus status;
    private LocalDate createdAt;
    private LoanRepaymentFrequency repaymentFrequency = LoanRepaymentFrequency.WEEKLY;
    @OneToMany(mappedBy = "loanApplicationId")
    private List<LoanRepayment> repayments;

}
