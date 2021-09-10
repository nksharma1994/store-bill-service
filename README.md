# store-bill-service

build:
mvn clean install

test report after build:
\target\site\jacoco\index.html

swagger:
http://localhost:8101/swagger-ui.html

endpoint:
POST store/bill/summary

Sample Request:
{
    "userId": 1004,
    "orderProductSummaries": [
    {
        "productId": 1,
        "quantity": 8
    },
    {
        "productId": 2,
        "quantity": 10
    }
    ]
}

Sample Response:
{
    "user": {
        "id": 1004,
        "name": "CUSTOMER User less than 2years",
        "type": "CUSTOMER",
        "joiningDate": "2020-11-15"
    },
    "productSummaries": [
    {
    "id": 1,
    "name": "Grocery Product 50",
    "categoryName": "Grocery",
    "quantity": 8,
    "price": 50,
    "grossPrice": 400
    },
    {
    "id": 2,
    "name": "Other Product 80",
    "categoryName": "Other",
    "quantity": 10,
    "price": 80,
    "grossPrice": 800
    }
    ],
    "grossPrice": 1200,
    "discount": 60,
    "netPayablePrice": 1140
}