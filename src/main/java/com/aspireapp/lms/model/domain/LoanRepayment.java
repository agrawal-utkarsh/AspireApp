package com.aspireapp.lms.model.domain;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoanRepayment {

    @Id
    private UUID id;
    private UUID loanApplicationId;
    private Double amount;
    private RepaymentStatus status;
    private LocalDate dueAt;

    public LoanRepayment(UUID loanApplicationId, Double amount, LocalDate dueDate) {
        this.id = UUID.randomUUID();
        this.loanApplicationId = loanApplicationId;
        this.amount = amount;
        this.status = RepaymentStatus.PENDING;
        this.dueAt = dueDate;
    }
}
