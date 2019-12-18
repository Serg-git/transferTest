# Account Transfer RESTful API

This is a sample application. It was made specifically to show the working concept of RESTful API for money
transfers between accounts and other basic operations.

This is a completely standalone app. Basicaly it is just an `executable JAR built with maven`.
To create RESTful web service I mainly used `Jersey` in combination with `tomcat embedded server` and `H2` 
as in-memory database. 

## Install

    mvn clean verify 

## Run 

    java -jar transferTest-${app.version}.jar 


It NO loads test data into the database automatically right after the start of this application. It means that you need
put your own custom Accounts data into the table.

So here is the list of requests you can try in this simple app:

### Add new account 
```
    POST localhost:8080/accounts/new
```
Payload example:
```
{
	"Id": "1",
	"balance": "23.00"
}

Response example:
```
    HTTP 200 
{
    "id": 1,
    "balance": "23.00"
}

### etrieve list of available accounts
```
    GET localhost:8080/accounts/all
```
Response example:
```
    HTTP 200 
[  
   {  
      "id":1,
      "balance":100
   },
   {  
      "id":2,
      "balance":200
   }
]
```
### Retrieve account by ID

```
    GET localhost:8080/accounts/2
```
Response example:

```
    HTTP 200 
{
    "id": 2,
    "balance": 200
}
```
... in case of a nonexistent ID:
```
    HTTP 404 
    {"timestamp":"2019-12-18T14:12:17.757+0000","message":"Account not found for this id :: 1","details":"uri=/accounts/1"}
    
```

### Transfer money from one account to another
```
    PUT localhost:8080/accounts/transfer
```
Payload example:
```
{
	"sourceId": "1",
	"targetId": "5",
	"amount": "23.00"
}
```
Response example:

```
    HTTP 200 
{
    "status": "SUCCESS",
    "code": 1
}
```
... in case of an insufficient balance:
```
    HTTP 200 
{
    "status": "FAIL",
    "code": 2,
    "message": "Not enough money to complete transfer operation. Current balance: 77.00, Requested amount: 223.00"
}
```
