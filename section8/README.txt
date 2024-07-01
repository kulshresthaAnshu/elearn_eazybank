This section covers the example of 
1. we have covered eureka server discovery and registry 
3. update docker compose

In this example we have removed the changes for MYSQL ,AMQP, Monitor.The below depedency to keep the project lightweight are removed. This will remove the need of Mysql container RabbitMQ container.
we have used the eureka for service registry and discovery.

4. To execute the schema.sql file we need to make sure that the table name is as same as entity

the docker compose file has two loan ms. if we do not hit the url for create loan using 8091 we may get the message while hitting url
http://localhost:8080/api/fetchCustomerDetails?mobileNumber=4354437687

as below 
{
    "timestamp": "2024-06-30T18:01:28.594+00:00",
    "status": 500,
    "error": "Internal Server Error",
    "trace": "feign.FeignException$NotFound: [404] during [GET] to [http://loans/api/fetch?mobileNumber=4354437687] [LoansFeignClient#fetchLoanDetails(String)]: [{\"apiPath\":\"uri=/api/fetch\",\"errorCode\":\"NOT_FOUND\",\"errorMsg\":\"Loan not found with the given java.lang.Thread.run(Unknown Source)\n",
    "message": "[404] during [GET] to [http://loans/api/fetch?mobileNumber=4354437687] [LoansFeignClient#fetchLoanDetails(String)]: [{\"apiPath\":\"uri=/api/fetch\",\"errorCode\":\"NOT_FOUND\",\"errorMsg\":\"Loan not found with the given input data mobileNumber : '4354437687'\",\"errorTime\":\"2024-06-30T18:01:28.573418528\"}]",
    "path": "/api/fetchCustomerDetails"
}

or as below
{
    "name": "Madan Reddy",
    "email": "tutor@eazybytes",
    "mobileNumber": "4354437687",
    "accountDto": {
        "accountNumber": 1084835160,
        "accountType": "savings",
        "branchAddress": "123 Main Street NewYork"
    },
    "loansDto": {
        "mobileNumber": "4354437687",
        "loanNumber": "100608736110",
        "loanType": "Home Loan",
        "totalLoan": 100000,
        "amountPaid": 0,
        "outstandingAmount": 100000
    },
    "cardsDto": {
        "mobileNumber": "4354437687",
        "cardNumber": "100352109555",
        "cardType": "Credit Card",
        "totalLimit": 100000,
        "amountUsed": 0,
        "availableAmount": 100000
    }
}

This shows the load balancing,as there are two instances but since we are using H2 DB this created different database of each service 8090 and 8091.
when it balances the load to 8091 internally we get exception else the message.