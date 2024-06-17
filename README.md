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
    --url http://localhost:8080/loanApplication/createLoanApplication \
    --header 'Content-Type: application/json' \
    --header 'User-Agent: insomnia/2023.5.8' \
    --data '{
    "amountRequired":1,
    "loanTerm":10,
    "userId":"46e5339f-e23d-4776-a513-3a354aafc3ea"
    }'
![image](https://github.com/agrawal-utkarsh/AspireApp/assets/14595816/549d5163-de05-4f71-8d27-73906c079de5)

### Approve Loan Application
    curl --request POST \
    --url http://localhost:8080/loanApplication/approveLoan \
    --header 'Content-Type: application/json' \
    --header 'User-Agent: insomnia/2023.5.8' \
    --data '{
    "loanId":"65e9a9f5-bb4c-4b10-9632-933e4e6066cd"
    }'
![image](https://github.com/agrawal-utkarsh/AspireApp/assets/14595816/0e00ea34-0559-4719-87b9-705bd33e4454)

### Get Loan Applications for User
    curl --request POST \
    --url http://localhost:8080/loanApplication/getLoanApplicationsForUser \
    --header 'Content-Type: application/json' \
    --header 'User-Agent: insomnia/2023.5.8' \
    --data '{
    "userId":"46e5339f-e23d-4776-a513-3a354aafc3ea"
    }'
![image](https://github.com/agrawal-utkarsh/AspireApp/assets/14595816/08f2f1d9-3822-4726-80f4-b93f9d01895a)

### Submit Loan Repayment
    curl --request POST \
    --url http://localhost:8080/loanApplication/submitLoanRepayment \
    --header 'Content-Type: application/json' \
    --header 'User-Agent: insomnia/2023.5.8' \
    --data '{
    "loanApplicationId":"65e9a9f5-bb4c-4b10-9632-933e4e6066cd",
    "amount":0.8
    }'
![image](https://github.com/agrawal-utkarsh/AspireApp/assets/14595816/2152d6d0-6592-4545-9736-965b224c6222)
