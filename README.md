## 📌 Sell Ecom

**Brief description of your Spring Boot application.**  
This is a RESTful API for managing orders in a e-commerce application and a account system to management authentication and authorization. 

---

## 📋 Table of Contents

- [📌 Sell Ecom](#-sell-ecom)
- [📋 Table of Contents](#-table-of-contents)
- [📖 About the Project](#-about-the-project)
- [🛠 Technologies Used](#-technologies-used)
- [🚀 Installation \& Setup](#-installation--setup)
  - [Prerequisites](#prerequisites)
  - [Steps](#steps)
- [⚙ Configuration](#-configuration)
  - [Environment Variables](#environment-variables)
- [▶ Running the Application](#-running-the-application)
- [🔥 API Endpoints](#-api-endpoints)
  - [Authentication](#authentication)
  - [Order Management](#order-management)
  - [Coupon Management](#coupon-management)
- [🧪 Testing](#-testing)
- [📜 License](#-license)
---

## 📖 About the Project

Provide an overview of the project, its purpose, and key features.  

> This project is a Spring Boot-based REST API for handling orders of a e-commerce system and the management of coupons, include operations of mutations like [Create Order, Cancel Order, Make Payment, Create Coupon] and Search Operations as well.
> It integrates with a MySql database using Spring data JDBC and Flyway for management migrations, it also uses Spring Security JWT and BasicAuth for authentication and Authorization.

---

## 🛠 Technologies Used

List the technologies used in the project:

- **Spring Boot** (version 3.4.2)
- **Spring Security**
- **Spring Data JDBC**
- **FlyWay** (for manager db migrations)
- **JavaMailSender** (For send confirmations and orders status by email)
- **MySQL** (Version 8.1)
- **Swagger (SpringDoc OpenAPI)**
- **Docker**
- **JUnit & Mockito & TestContainers** (for testing)

---

## 🚀 Installation & Setup

### Prerequisites
- Java 17+ (Ensure it's installed: `java -version`)
- Maven (`mvn -v`)
- MySQL (for database)

### Steps
1. Clone the repository:
   ```sh
   git clone https://github.com/LeandroSantosP/sell-ecom.git
   cd sell-ecom
   ```
---
## ⚙ Configuration

### Environment Variables

2. Create `.env` file in the root of the project and pass config of the your smtp provider:
```sh
    touch .env
```

SUPPORT_EMAIL=

APP_PASSWORD=   

tutorial video for how to integration with gmail smtp:

https://www.youtube.com/watch?v=kLMUS0-PznE&t=47s&ab_channel=GenuineCoder

---
1. Inside of .env file in the root of the project, pass the follow db config("it will automatic to application.properties) or if you will use `docker compose` than you need to run the container before you run the application:

DB_NAME=

DB_USERNAME=

DB_PASSWORD=

DB_PORT=

DB_HOST=localhost

---

4. Install dependencies:
```sh
mvn clean install
```
---

## ▶ Running the Application

Run the application using Maven:

```sh
mvn spring-boot:run
```

Or with Java:

```sh
java -jar target/your-app.jar
```

The application will start on `http://localhost:8080`.

---

## 🔥 API Endpoints

For detailed API documentation, visit `http://localhost:8080/swagger-ui.html`.

### Authentication

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/auth/signup` | Register a new user |
| `POST` | `/api/auth/signin` | Authenticate a user |
| `POST` | `/api/auth/verify` | Verify with the verification code |
| `POST` | `/api/auth/resend` | Resend the verification code (email) |
| `POST` | `/api/auth/priv/bestow-role/{user_id}` | Give an role for an user like admin (just admins allowed) |

### Order Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/orders/place-order` | Create a order |
| `POST` | `/api/orders/make-payment/{order_id}` | Make the payment of an order |
| `POST` | `/api/orders/cancel-order/{order_id}` | Cancel an order |
| `GET` | `/api/orders/{client_id}/{status}` | Get an order base the client_id and order status |
| `GET` | `/api/orders/get-order/{order_id}` | Get an Order |


### Coupon Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/coupons` | Create a new coupon |
| `GET` | `/api/coupon/{code}` | Get an coupon  |

For detailed API documentation, visit `http://localhost:8080/swagger-ui.html`.

---

## 🧪 Testing

Run unit and integration tests using:

```sh
mvn test
```
---

## 📜 License

This project is licensed under the **MIT License** – see the [`LICENSE`](LICENSE) file for details.

---