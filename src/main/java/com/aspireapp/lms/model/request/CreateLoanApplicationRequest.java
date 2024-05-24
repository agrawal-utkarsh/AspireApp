package com.aspireapp.lms.model.request;

import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class CreateLoanApplicationRequest {
    private UUID userId;
    private Double amountRequired;
    private Integer loanTerm;
}
