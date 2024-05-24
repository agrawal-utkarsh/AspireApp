package com.aspireapp.lms.model.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @ToString
public class User {
	
	private UUID id;
	private List<LoanApplication> loans;

}
