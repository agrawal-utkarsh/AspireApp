# AspireApp - Lending Management System

This project is a lending management system with a base set of functionalities like -

1. User can ask for loan
2. Admin can approve loan
3. User can see their loans
4. User can repay their loans

It is written in **Java** programming language with **Spring Boot** framework.  
It uses an in-memory database, **H2** for DB interactions.

## Project Structure

1. **Controller** contains the REST interfaces that client interacts with
2. **Exception** contains exception classes used across the codebase
3. **Model** contains class definitions for the various entities used in the project
4. **Repository** contains DAO classes that interface with the DB to fetch and store data
5. **Service** contains the actual business logic
6. **Utils** contains helper classes
7. **test** folder contains the unit test cases

### Execution Flow of code
Controller -> Service + Utils -> Repository

## Entities identified

- User
- LoanApplication
- LoanRepayment

A user can have multiple loan applications, so a **one to many** relationship exists here  
A loan application can have multiple loan repayments, so a **one to many** relationship exists here

## Api Screenshots
### Create Loan Application
    curl --request POST \
    --url http://localhost:8080/loan-applications/createLoanApplication \
    --header 'Content-Type: application/json' \
    --header 'User-Agent: insomnia/2023.5.8' \
    --data '{
    "amountRequired":1,
    "loanTerm":10,
    "userId":"46e5339f-e23d-4776-a513-3a354aafc3ea"
    }'

### Approve Loan Application
    curl --request PUT \
    --url http://localhost:8080/loan-applications/8dc0d87a-0f46-4105-ac65-0a33b761fee5/approve \
    --header 'User-Agent: insomnia/2023.5.8'
### Get Loan Applications for User
    curl --request GET \
    --url http://localhost:8080/loan-applications/user/46e5339f-e23d-4776-a513-3a354aafc3ea \
    --header 'User-Agent: insomnia/2023.5.8'

### Submit Loan Repayment
    curl --request POST \
    --url http://localhost:8080/loan-applications/8dc0d87a-0f46-4105-ac65-0a33b761fee5/repayments \
    --header 'Content-Type: application/json' \
    --header 'User-Agent: insomnia/2023.5.8' \
    --data '{
    "amount":0.8
    }'
