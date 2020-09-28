#### build the app :
    mvn clean install
	
#### run the server

	mvn spring-boot:run
	
#### run the test
    mvn test
    
### DB
    Runs locally with embeded h2 
    http://localhost:8080/h2-console
    
#### execute as follow

Create a Wallet

    curl --location --request POST 'http://localhost:8080/api/v1/wallet' \
    --header 'Authorization: Basic dXNlcjpwYXNz' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "name" : "dummy wallet 1"
    }'

Edit a Wallet

    curl --location --request PUT 'http://localhost:8080/api/v1/wallet/1' \
    --header 'Authorization: Basic dXNlcjpwYXNz' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "name" : "dummy wallet edit"
    }'
    
View all Wallet

    curl --location --request GET 'http://localhost:8080/api/v1/wallet' \
    --header 'Authorization: Basic dXNlcjpwYXNz'
    
View a Wallet

    curl --location --request GET 'http://localhost:8080/api/v1/wallet/1' \
    --header 'Authorization: Basic dXNlcjpwYXNz' 
    
Add fund to a Wallet

    curl --location --request POST 'http://localhost:8080/api/v1/wallet/1/topup' \
    --header 'Authorization: Basic dXNlcjpwYXNz' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "fund_amount": 30.1
    }'

Withdraw fund from a Wallet

    curl --location --request POST 'http://localhost:8080/api/v1/wallet/1/withdraw' \
    --header 'Authorization: Basic dXNlcjpwYXNz' \
    --header 'Content-Type: application/json' \
    --data-raw '{
        "fund_amount": 1.01
    }'
    