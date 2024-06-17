package com.aspireapp.lms.controller;

import com.aspireapp.lms.exception.ValidationException;
import com.aspireapp.lms.model.request.CreateLoanApplicationRequest;
import com.aspireapp.lms.model.request.SubmitLoanRepaymentRequest;
import com.aspireapp.lms.model.response.GetLoanApplicationsForUserResponse;
import com.aspireapp.lms.service.LoanApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/loan-applications")
@Slf4j
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    @PostMapping("/createLoanApplication")
    public ResponseEntity<String> createLoanApplication(@RequestBody CreateLoanApplicationRequest request) {
        try {
            loanApplicationService.createLoanApplication(request);
            return new ResponseEntity<>("Loan Application created successfully", HttpStatus.CREATED);
        } catch (ValidationException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{loanApplicationId}/approve")
    public ResponseEntity<String> approveLoan(@PathVariable UUID loanApplicationId) {
        try {
            loanApplicationService.approveLoan(loanApplicationId);
            return new ResponseEntity<>("Loan Application approved successfully", HttpStatus.OK);
        } catch (ValidationException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<GetLoanApplicationsForUserResponse> getLoanApplicationsForUser(@PathVariable UUID userId) {
        GetLoanApplicationsForUserResponse response = new GetLoanApplicationsForUserResponse(loanApplicationService.getLoanApplicationsForUser(userId));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{loanApplicationId}/repayments")
    public ResponseEntity<String> submitLoanRepayment(@PathVariable UUID loanApplicationId, @RequestBody SubmitLoanRepaymentRequest request) {
        try {
            loanApplicationService.submitLoanRepayment(loanApplicationId, request);
            return new ResponseEntity<>("Loan Repayment submitted successfully", HttpStatus.OK);
        } catch (ValidationException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
