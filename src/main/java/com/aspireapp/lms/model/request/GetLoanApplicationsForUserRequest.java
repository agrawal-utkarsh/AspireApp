package com.aspireapp.lms.model.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@Getter
@NoArgsConstructor
@ToString
public class GetLoanApplicationsForUserRequest {
    private UUID userId;
}
