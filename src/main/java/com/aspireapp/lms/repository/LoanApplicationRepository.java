package com.aspireapp.lms.repository;

import com.aspireapp.lms.model.domain.LoanApplication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface LoanApplicationRepository extends CrudRepository<LoanApplication, UUID> {
    List<LoanApplication> findAllByUserId(UUID userId);
}
