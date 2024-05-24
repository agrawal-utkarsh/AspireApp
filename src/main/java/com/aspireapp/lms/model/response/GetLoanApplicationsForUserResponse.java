package com.aspireapp.lms.model.response;

import com.aspireapp.lms.model.domain.LoanApplication;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetLoanApplicationsForUserResponse {
    private List<LoanApplication> loanApplications;
}
