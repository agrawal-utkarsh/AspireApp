package com.aspireapp.lms.model.request;

import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
@Setter
public class SubmitLoanRepaymentRequest {
    private Double amount;
}