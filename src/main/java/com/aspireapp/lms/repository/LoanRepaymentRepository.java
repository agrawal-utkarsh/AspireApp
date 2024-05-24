package com.aspireapp.lms.repository;

import com.aspireapp.lms.model.domain.LoanRepayment;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface LoanRepaymentRepository extends CrudRepository<LoanRepayment, UUID> {
}
