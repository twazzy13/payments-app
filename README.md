# payments-app
Application for storing and receiving transactions in their respective currency

## Setup
In order to run this project you will need:
 * Java 25 or higher
 * Apache Maven 3.9.11 or higher
 * git

Download the project using git

Build the project using
    $ mvn clean install



## Endpoints

### /api/v1/purchase
    * `POST` Create a new purchase transaction
#### Request 

| Field                          | Data Type |                                                                                    Requirements |
|:-------------------------------|:---------:|------------------------------------------------------------------------------------------------:|
| description                    |  String   |                                 This field cannot be empty, and must have 50 characters or less |
| purchaseTransactionDate        |   Date    | This field cannot be empty, must be provided be in UTC and must be format yyyy-MM-dd'T'HH:mm:ss |
| purchaseAmount                 |  Number   |           This is the purchase amount in USD. This field cannot be empty and cannot be negative |


#### Response

| Field                   | Data Type |                                                         Description |
|:------------------------|:---------:|--------------------------------------------------------------------:|
| id                      |   long    |                              This field will return the purchase id |
| description             |  String   |                     This field will return the purchase description |
| purchaseTransactionDate |   Date    | This field cannot be empty and must be format yyyy-MM-dd'T'HH:mm:ss |
| purchaseAmount          |  Number   |        This field will return USD purchase amount with two decimals |


Example Request Body:
```
{
    
    "description": "Test Description",
    "purchaseTransactionDate": "2025-09-30T00:00:00",
    "purchaseAmount": 1000000000000.3344
}
```
Example Response:
```
{
    "description": "testtesttesttesttesttesttesttesttesttesttesttestte",
    "id": 202,
    "purchaseAmount": 1000000000000.33,
    "purchaseTransactionDate": "2025-09-30T00:00:00"
}
```

### /api/v1/purchase/{id}
    * `GET` Get a transaction by id and return a currency conversion for the transaction from USD to chosen coversion
#### Request

The request requires a value for either country or currency, both can be provided as well.

| Field          | Data Type |                                                         Requirements |
|:---------------|:---------:|---------------------------------------------------------------------:|
| country        |  String   |  This value is the country to which currency should be converted to. |
| currency       |  String   | This value is the currency to which purchase should be converted to. |    


#### Response

| Field                      | Data Type |                                                         Description |
|:---------------------------|:---------:|--------------------------------------------------------------------:|
| id                         |   long    |                              This field will return the purchase id |
| description                |  String   |                     This field will return the purchase description |
| purchaseTransactionDate    |   Date    | This field cannot be empty and must be format yyyy-MM-dd'T'HH:mm:ss |
| purchaseAmountUSD          |  Number   |        This field will return USD purchase amount with two decimals |
| exchangeRate               |  Number   |                               The exchange rate to convert from USD |
| convertedPurchaseAmount    |  Number   |           The converted currency amount rounded to the nearest cent |

Example Request :
http://localhost:8080/api/v1/purchase/1
Body:
```
{
    "country": "YourCountry"
}
```
Example Response:
```
{
    "id": 1,
    "description": "test",
    "purchaseTransactionDate": "2025-09-30T00:00:00",
    "purchaseAmountUSD": 1000000000000.33,
    "exchangeRate": 67.33,
    "convertedPurchaseAmount": 67330000000022.22
}
```

## H2 Database

The h2 console can be accessed locally default:
    $ http://localhost:8080/h2

The url can but update in the application.properties file here:
spring.h2.console.path=/h2

Update the JDBC to be the the location of the database. 
By default: jdbc:h2:${project base dir}/purchaseTransaction-app-db


The location of the database can be updating the property:
    $ spring.datasource.url=jdbc:h2:${Your_DIR}


## Actuator 
This project has the spring boot actuator configured and a health check can be performed:
http://localhost:8080/actuator/health



## Docker
 * Required Docker to be installed


### Getting started
    
To build a docker image, first build the project:

    $ mvn clean install

To build a docker image, provided the version of the app your just built
    
    $ Docker build . --build-arg APP_VERSION=0.0.1-SNAPSHOT -t payment-app:latest

Run the container on port 8080:

    $ docker run -d -p 8080:8080 --name payment-app-5  payment-app:latest

To stop the image, find the container id:

    $ docker ps
    $ docker stop ${container id}
