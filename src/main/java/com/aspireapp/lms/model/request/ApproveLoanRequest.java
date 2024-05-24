package com.aspireapp.lms.model.request;

import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ApproveLoanRequest {
    private UUID loanId;
}
