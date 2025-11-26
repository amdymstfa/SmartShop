Parfait ! Je vais mettre à jour ton README en anglais et y intégrer le diagramme de classes complet avec `Customer` à la place de `Client`. Voici la version révisée :

---

```markdown
# 🛒 SmartShop - B2B Commercial Management System

![Java](https://img.shields.io/badge/Java-8%2B-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![Liquibase](https://img.shields.io/badge/Liquibase-4.20-red)
![Maven](https://img.shields.io/badge/Maven-3.8%2B-purple)

## 📋 Description

SmartShop is a web-based commercial management application for **MicroTech Morocco**, a B2B distributor of IT equipment based in Casablanca.

The application manages:
- ✅ 650 active customers
- ✅ Loyalty system with progressive discounts
- ✅ Multi-method split payments per invoice
- ✅ Full traceability of financial events
- ✅ Optimized cash flow management

## 🏗️ Architecture

### **Backend REST API**
- REST API only (no frontend)
- Testing via Postman / Swagger
- HTTP Session Authentication (no JWT)
- Layered architecture (Controller → Service → Repository)

### **Technical Stack**
- **Framework**: Spring Boot 2.7.18
- **Language**: Java 8+
- **Database**: PostgreSQL / MySQL
- **Migrations**: Liquibase
- **ORM**: Spring Data JPA / Hibernate
- **Mapping**: MapStruct
- **Validation**: Bean Validation
- **Documentation**: SpringDoc OpenAPI (Swagger)
- **Testing**: JUnit 5, Mockito

## 📂 Project Structure

```

smartshop/
├── src/main/java/com/microtech/smartshop/
│   ├── config/          # Configurations
│   ├── entity/          # JPA Entities
│   ├── enums/           # Enumerations
│   ├── repository/      # Spring Data Repositories
│   ├── service/         # Business Services
│   ├── dto/             # Data Transfer Objects
│   ├── mapper/          # MapStruct Mappers
│   ├── controller/      # REST Controllers
│   ├── util/            # Utilities
│   ├── exception/       # Error Handling
│   └── interceptor/     # HTTP Interceptors
│
├── src/main/resources/
│   ├── application.yml              # Main config
│   ├── application-dev.yml          # Dev profile
│   ├── application-prod.yml         # Prod profile
│   └── db/changelog/                # Liquibase migrations
│       ├── db.changelog-master.yaml
│       └── changes/
│
└── src/test/                        # Unit & integration tests

````

## 🚀 Installation

### **Prerequisites**
- Java 8+ (JDK)
- Maven 3.8+
- PostgreSQL 15+ or MySQL 8+
- Git

### **Clone the project**
```bash
git clone https://github.com/microtech/smartshop.git
cd smartshop
````

### **Database setup**

```bash
# PostgreSQL
createdb smartshop_db

# Edit application-dev.yml with your credentials
```

### **Run the application**

```bash
# Development mode
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Or build and run
mvn clean install
java -jar target/smartshop-1.0.0.jar --spring.profiles.active=dev
```

### **Access Swagger**

```
http://localhost:8080/swagger-ui.html
```

## 📊 Main Features

### **1. Customer Management**

* Full CRUD for customers
* Automatic statistics tracking (orders, total spent)
* Customer order history

### **2. Automatic Loyalty System**

* **BASIC**: Default tier
* **SILVER**: 3 orders OR 1,000 DH total
* **GOLD**: 10 orders OR 5,000 DH total
* **PLATINUM**: 20 orders OR 15,000 DH total

Automatic discounts by tier:

* SILVER: 5% (for orders ≥ 500 DH)
* GOLD: 10% (for orders ≥ 800 DH)
* PLATINUM: 15% (for orders ≥ 1,200 DH)

### **3. Product Management**

* CRUD with soft delete
* Real-time stock management
* Filtering and pagination

### **4. Order Management**

* Multi-product orders
* Automatic calculation: subtotal, discount, 20% VAT, total
* Status: PENDING, CONFIRMED, CANCELED, REJECTED
* Promo code support (format: PROMO-XXXX)

### **5. Multi-Method Payments**

* Types: CASH, CHECK, TRANSFER
* Legal cash limit: 20,000 DH
* Split payments per order
* Full traceability (dates, references, statuses)

### **6. Business Rules**

* Stock validation before order
* Order can only be confirmed if fully paid
* Automatic loyalty tier update after confirmation
* Centralized exception handling

## 🧪 Testing

```bash
# Unit tests
mvn test

# Test coverage report
mvn test jacoco:report

# Integration tests
mvn verify
```

## 📦 Build

```bash
# Build without tests
mvn clean package -DskipTests

# Full build
mvn clean install
```

## 🔧 Configuration

### **Profiles**

* `dev` : Local development
* `prod` : Production
* `test` : Automated tests

### **Environment Variables**

```yaml
# VAT rate (configurable)
app.tax.vat-rate: 0.20

# Cash payment limit
app.payment.cash-limit: 20000.00

# Pagination
app.pagination.default-page-size: 10
app.pagination.max-page-size: 100
```

## 🗃️ Database

### **Liquibase Migrations**

```bash
# Check status
mvn liquibase:status

# Rollback
mvn liquibase:rollback -Dliquibase.rollbackCount=1
```

### **Main Tables**

* `users` : Users (ADMIN/CLIENT)
* `customers` : B2B customers
* `products` : Products
* `orders` : Orders
* `order_items` : Order line items
* `payments` : Payments
* `promo_codes` : Promo codes
* `tier_history` : Loyalty tier history

---

## 📝 UML Class Diagram

```text
# Entities

Customer
- id: Long
- name: String
- email: String
- tier: CustomerTier
- totalOrders: Integer
- totalSpent: BigDecimal
- firstOrderDate: LocalDateTime
- lastOrderDate: LocalDateTime
- isDeleted: Boolean
- createdAt: LocalDateTime
- updatedAt: LocalDateTime

User
- id: Long
- username: String
- password: String
- role: UserRole
- createdAt: LocalDateTime
- updatedAt: LocalDateTime

Product
- id: Long
- name: String
- description: String
- unitPrice: BigDecimal
- stock: Integer
- isDeleted: Boolean
- createdAt: LocalDateTime
- updatedAt: LocalDateTime

Order
- id: Long
- customer: Customer
- createdAt: LocalDateTime
- subtotalExcludingTax: BigDecimal
- discountAmount: BigDecimal
- amountAfterDiscountExclTax: BigDecimal
- taxAmount: BigDecimal
- totalIncludingTax: BigDecimal
- promoCode: String
- status: OrderStatus
- amountRemaining: BigDecimal
- items: List<OrderItem>
- payments: List<Payment>

OrderItem
- id: Long
- order: Order
- product: Product
- quantity: Integer
- unitPrice: BigDecimal
- subTotal: BigDecimal

Payment
- id: Long
- order: Order
- paymentNumber: Integer
- amount: BigDecimal
- paymentType: PaymentType
- paymentDate: LocalDateTime
- clearanceDate: LocalDateTime
- reference: String
- bank: String
- dueDate: LocalDate
- status: PaymentStatus

PromoCode
- id: Long
- code: String
- discountPercentage: BigDecimal
- expirationDate: LocalDate
- used: Boolean
- createdAt: LocalDateTime

TierHistory
- id: Long
- customer: Customer
- oldTier: CustomerTier
- newTier: CustomerTier
- changeDate: LocalDateTime
- reason: String
```

---

## 👥 Roles & Permissions

### **ADMIN** (MicroTech employee)

* Full CRUD: customers, products, orders
* Payment registration
* Order confirmation/cancellation
* View all data

### **CLIENT** (Customer company)

* View own profile and stats
* View own orders
* Browse product catalog (read-only)

---

## 📖 API Documentation

### **Swagger UI**

```
http://localhost:8080/swagger-ui.html
```

### **OpenAPI JSON**

```
http://localhost:8080/api-docs
```

### **Postman Collection**

Available at `/docs/postman/SmartShop.postman_collection.json`

---

## 🐛 Error Handling

All endpoints return structured JSON errors:

```json
{
  "timestamp": "2025-11-25T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Customer with ID 999 does not exist",
  "path": "/api/customers/999"
}
```

### **HTTP Codes**

* `200` : Success
* `201` : Created
* `400` : Validation error
* `401` : Unauthorized
* `403` : Forbidden
* `404` : Not found
* `422` : Business rule violation
* `500` : Server error

---

## 🤝 Contribution

### **Commit Convention**

```
feat: New feature
fix: Bug fix
docs: Documentation
refactor: Refactoring
test: Tests
chore: Maintenance
```

### **Branches**

* `main` : Production
* `develop` : Development
* `feature/xxx` : New feature
* `hotfix/xxx` : Urgent fix

---

## 📜 License

Property of **MicroTech Morocco** - All rights reserved

## 👨‍💻 Authors

* **MicroTech Team** - Initial development

## 📞 Contact

* **Email**: [contact@microtech.ma](mailto:contact@microtech.ma)
* **Website**: [https://www.microtech.ma](https://www.microtech.ma)
* **Support**: [support@microtech.ma](mailto:support@microtech.ma)

---

**Version**: 1.0.0
**Last update**: November 25, 2025
**Status**: 🚧 Active development

****
