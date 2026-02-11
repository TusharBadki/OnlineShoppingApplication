##  1. Overview

This project is a Java, Spring Boot–based backend application.
It models an online grocery shop that manages products, packaging options, and processes customer orders by calculating the optimal packaging combination with minimal number of packages and best pricing.


##  2. Problem Statement Summary

The system supports:

-	Product management (CRUD operations)

-	Packaging options with discounted bundle pricing

-	Order processing where customers order products by quantity

-	Calculation of total cost using optimal packaging to minimise shipping packages


##  3. Tech Stack Used

   -	Java 17

   -	Spring Boot 3.x

   -	Spring Data JPA (Hibernate)

   -	MySQL 9.6

   -	REST APIs

   -	Maven 3.9

   -	JUnit & Mockito (unit testing)

   -	Git (version control)


##  4. Application Architecture

The application follows a standard layered architecture:

-	Controller Layer

Handles HTTP requests and responses

-	Service Layer

Contains business logic (pricing calculation, packaging optimisation)
-	Repository Layer

Database access using Spring Data JPA
-	DTO Layer

Request and response objects to decouple API from entities


##  5. Database Design


### Product

| Column Name     | Data Type     | Description                    |
|-----------------|---------------|--------------------------------|
| product_code    | VARCHAR(10)   | Unique product identifier      |
| product_name    | VARCHAR(50) | Product name                   |
| unit_price      | DECIMAL(10,2) | Unit price                     |


### Packaging Option

| Column Name       | Data Type        | Description                                  |
|-------------------|------------------|----------------------------------------------|
| product_code      | VARCHAR(10)      | Foreign key → Product(product_code)          |
| quantity  | INT              | Quantity per package                         |
| package_price     | DECIMAL(10,2)    | Discounted price for the package             |



##  6. API Endpoints:

Product APIs:
```declarative
POST /api/products – Create a product

GET /api/products – Get all products

GET /api/products/{code} – Get product by code

PUT /api/products/{code} – Update product

DELETE /api/products/{code} – Delete product
```

Packaging Options APIs:
```declarative
POST /api/packaging-option – Create a packaging option

GET /api/packaging-option  – Get all packaging options

GET /api/packaging-option/{code}/{quantity} – Get packaging option by code and quantity

PUT /api/packaging-option/{code}/{quantity} – Update packaging option by code and quantity

DELETE /api/packaging-option/{code} – Delete product
```

Order API
```declarative
POST /api/grocery/orders – Process customer order
```


Sample Request:

```json
{
  "items": {
    "CE": 10,
    "HM": 14,
    "SS": 3
  }
}
```

Sample Response:
```json
{
    "itemList": [
        {
            "productCode": "CE",
            "requestedQuantity": 10,
            "totalPrice": 41.90
        },
        {
            "productCode": "HM",
            "requestedQuantity": 14,
            "totalPrice": 78.85
        },
        {
            "productCode": "SS",
            "requestedQuantity": 3,
            "totalPrice": 35.85
        }
    ],
    "totalOrderCost": 156.60
}
```

Sample log content:
```terminaloutput
2026-02-11 23:37:34 [http-nio-8085-exec-2] INFO  c.p.o.shopping.service.OrderService - 2 packages of 5 items ($20.95 each)
2026-02-11 23:37:34 [http-nio-8085-exec-2] INFO  c.p.o.shopping.service.OrderService - 10 CE for $41.90
2026-02-11 23:37:34 [http-nio-8085-exec-2] INFO  c.p.o.shopping.service.OrderService - 1 packages of 8 items ($40.95 each)
2026-02-11 23:37:34 [http-nio-8085-exec-2] INFO  c.p.o.shopping.service.OrderService - 1 packages of 5 items ($29.95 each)
2026-02-11 23:37:34 [http-nio-8085-exec-2] INFO  c.p.o.shopping.service.OrderService - 1 packages of 1 items ($7.95 each)
2026-02-11 23:37:34 [http-nio-8085-exec-2] INFO  c.p.o.shopping.service.OrderService - 14 HM for $78.85
2026-02-11 23:37:34 [http-nio-8085-exec-2] INFO  c.p.o.shopping.service.OrderService - 3 packages of 1 items ($11.95 each)
2026-02-11 23:37:34 [http-nio-8085-exec-2] INFO  c.p.o.shopping.service.OrderService - 3 SS for $35.85
```

##  7. Business Logic
   -	Packaging options are sorted by quantity in descending order
   -	Maximum possible packages are applied first
   -	Remaining quantity is priced using unit price

   - This ensures:

   i)	Minimal number of packages

   ii)	Optimal pricing for the customer



##  8. How to Run the Application

Prerequisites:
-	Java 17
-	Maven
-	MySQL

Steps
1.	Clone the repository
```shell
git clone https://github.com/TusharBadki/OnlineShoppingApplication.git
```

2. Configure database in application.yml
```yaml
spring:
  application:
    name: online-shopping
  datasource:
    url: jdbc:mysql://localhost:3306/groceryshopping_db
    username: root
    password: password
```
3.	Run the application
```shell
mvn spring-boot:run
```
4. Application runs on:
```java
http://localhost:8085/online-shopping/api/
```



##  9. Testing
   -	Unit tests are written using JUnit and Mockito
   -    Web layer tests using WebMvcTest

Run tests:
```shell
mvn test
```


##  10. Assumptions & Notes

- Product codes are unique
- Packaging options are pre-configured per product
- Orders with invalid product codes result in error response
- If Product is removed from product table then it also should removed from the Packaging Option table : Handling done via table relation
- Focus is on backend logic and correctness rather than UI

##  11. Postman Collection

A Postman collection is provided to test all available APIs
```editorconfig
Location: online-shopping/postman/OnlineShoppingApplication.postman_collection.json
```

##  12. Database Script

Database creation, table creation, and some related queries provided in SQL Script file
```editorconfig
Location: /src/main/resources/DB_Scripts/online_shopping_db_scripts.sql
```