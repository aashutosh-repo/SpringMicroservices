# 🏦 SpringMicroservices

SpringMicroservices is a modular banking application demonstrating a microservice architecture using **Spring Boot**, **Spring Cloud**, and related technologies.  
Each service focuses on a specific domain like account management, customer details, payments, secure core operations, service discovery (Eureka), and API gateway routing.

> ✅ Designed for local development and testing, simulating a realistic banking backend.

---

## 📚 Table of Contents

- [Architecture](#architecture)
- [Microservice Modules](#microservice-modules)
  - [1. Account Service](#1-account-service)
  - [2. Customer Service](#2-customer-service)
  - [3. Payments Service](#3-payments-service)
  - [4. Core Service](#4-core-service)
  - [5. Eureka Service](#5-eureka-service)
  - [6. Gateway Service](#6-gateway-service)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Security](#security)
- [Development](#development)
- [Contributing](#contributing)
- [License](#license)

---

## 🧱 Architecture

- ✅ Spring Boot microservices for modular separation  
- ✅ Spring Cloud Eureka for service registration/discovery  
- ✅ Spring Cloud Gateway for routing and entry-point management  
- ✅ RESTful APIs for inter-service communication  
- ✅ WebClient for service-to-service calls  
- ✅ Spring Cache for performance optimization  
- ✅ Swagger/OpenAPI for API documentation  

---

## 🧩 Microservice Modules

### 1. 📂 Account Service

Handles all customer account operations.

- **CRUD for Accounts**: Create, update, delete, and view accounts.
- **Pending Auth Workflow**: For account creation/modification.
- **Account Types**: Supports savings, current, internal, business, loan, trade, etc.
- **Customer Integration**: Pulls customer data during account creation.

**API Examples**:
```http
GET /account/viewAccounts
GET /account/viewAccountByCustomerId?customerId=...
POST /account/create
```

---

### 2. 👤 Customer Service

Manages customer data and profiles.

- Customer create/update with address, KYC docs, nominee links
- Advanced search by ID, name, status, date range
- Optimized with caching

**API Examples**:
```http
POST /customer/create
GET /customer/findCustomerByMobileNumber?mobile=...
GET /customer/getAllCust
```

---

### 3. 💸 Payments Service

Handles transactions and histories.

- List transactions per account/customer
- Initiate and process payments securely
- Encrypts data via Core Service

**API Examples**:
```http
GET /payments/getPaymentDetails
GET /payments/initiate?customerId=...&accountId=...
POST /payments/process-payment
```

---

### 4. 🔐 Core Service

Provides secure backend operations.

- AES **Encryption/Decryption**
- Card/token **Tokenization**
- **EMI Calculator**
- Loads bank holidays from CSV

**API Examples**:
```http
POST /core/encrypt
POST /core/decrypt
POST /core/instruments/emiCalculator
POST /core/secureCard/tokenize
GET /core/secureCard/detokenize
```

---

### 5. 📡 Eureka Service

Service registry for discovery.

- Spring Cloud Eureka Server
- Access at `/eureka`
- Enables dynamic discovery for services

---

### 6. 🚪 Gateway Service

API gateway for unified routing and access.

- Spring Cloud Gateway
- Routes external URIs to backend services
- CORS support for Angular frontend
- Security (optional): OAuth2, JWT (config in comments)

---

## 🚀 Getting Started

### 🧰 Prerequisites

- Java 17+
- Maven/Gradle
- Docker (optional)
- Free localhost ports: `8080+`

---

### 🖥️ Running Locally

1. **Start Eureka**  
   Run `EurekaApplication` from `eureka/` module

2. **Start Gateway**  
   Run `GatewayApplication` from `gateway/` module

3. **Start Services**  
   Run main class from:
   - `account/`
   - `customer/`
   - `payments/`
   - `core/`

4. **Check Swagger UI**  
   Open `/swagger-ui` (on each service or via gateway, if enabled)

---

### 🔁 Inter-Service Communication

- Services use **REST APIs** via **Spring WebClient**
- All are discoverable through **Eureka**

---

## 📡 API Endpoints

### 🔸 Account Service

- `GET /account/viewAccounts`
- `GET /account/viewAccountByCustomerId?customerId=...`
- `POST /account/create`

### 🔸 Customer Service

- `POST /customer/create`
- `GET /customer/findCustomerByMobileNumber?mobile=...`
- `GET /customer/getAllCust`

### 🔸 Payments Service

- `GET /payments/getPaymentDetails`
- `POST /payments/process-payment`
- `GET /payments/initiate?customerId=...&accountId=...`

### 🔸 Core Service

- `POST /core/encrypt`
- `POST /core/decrypt`
- `POST /core/instruments/emiCalculator`
- `POST /core/secureCard/tokenize`
- `GET /core/secureCard/detokenize`

---

## 🔐 Security

- 🔐 **Encryption**: Core provides AES encryption/decryption
- 🪪 **Tokenization**: Secures sensitive card/token data
- 🔐 **Authentication** (optional): JWT-based login, OAuth2, basic auth
- 🌐 **CORS**: Pre-configured for Angular/React frontend access

---

## 🛠 Development

- Standard Spring Boot services with Maven or Gradle
- Each service runs independently
- API Docs via **Swagger/OpenAPI**
- Unit tests in `src/test/java`

---

## 🤝 Contributing

Contributions are welcome!  
Fork the repo and submit a pull request with clear changes and descriptions.

---

## 📄 License

This project is open source. See [LICENSE](./LICENSE) for details.

---

## 🙌 Credits

Built by **@aashutosh-repo** and contributors.

---

> 💡 For Docker or Kubernetes deployment, services can be containerized and orchestrated using `docker-compose` or Helm.
