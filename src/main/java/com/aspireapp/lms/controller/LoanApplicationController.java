package com.aspireapp.lms.controller;

import com.aspireapp.lms.exception.ValidationException;
import com.aspireapp.lms.model.request.ApproveLoanRequest;
import com.aspireapp.lms.model.request.CreateLoanApplicationRequest;
import com.aspireapp.lms.model.request.GetLoanApplicationsForUserRequest;
import com.aspireapp.lms.model.request.SubmitLoanRepaymentRequest;
import com.aspireapp.lms.model.response.GetLoanApplicationsForUserResponse;
import com.aspireapp.lms.service.LoanApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/loanApplication")
@Slf4j
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    @RequestMapping(method = RequestMethod.POST, value = "/createLoanApplication")
    public ResponseEntity<String> createLoanApplication(@RequestBody CreateLoanApplicationRequest request) {
        try {
            loanApplicationService.createLoanApplication(request);
            return new ResponseEntity<>("Loan Application created successfully", HttpStatus.OK);
        } catch (ValidationException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/approveLoan")
    public ResponseEntity<String> approveLoan(@RequestBody ApproveLoanRequest request) {
        try {
            loanApplicationService.approveLoan(request);
            return new ResponseEntity<>("Loan Application approved successfully", HttpStatus.OK);
        } catch (ValidationException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/getLoanApplicationsForUser")
    public ResponseEntity<GetLoanApplicationsForUserResponse> getLoanApplicationsForUser(@RequestBody GetLoanApplicationsForUserRequest request) {
        GetLoanApplicationsForUserResponse response = new GetLoanApplicationsForUserResponse(loanApplicationService.getLoanApplicationsForUser(request));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/submitLoanRepayment")
    public ResponseEntity<String> submitLoanRepayment(@RequestBody SubmitLoanRepaymentRequest request) {
        try {
            loanApplicationService.submitLoanRepayment(request);
            return new ResponseEntity<>("Loan Repayment submitted successfully", HttpStatus.OK);
        } catch (ValidationException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

}
