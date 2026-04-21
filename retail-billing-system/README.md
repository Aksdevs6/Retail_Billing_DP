# 🧵 Retail Billing System — Textile Shop
### Capstone Project | Full-Stack Java (Spring Boot + MySQL + HTML/CSS/JS)

---

## 📁 Project Folder Structure

```
retail-billing-system/
│
├── backend/                          ← Spring Boot Application
│   ├── pom.xml                       ← Maven dependencies
│   └── src/main/
│       ├── java/com/textile/billing/
│       │   ├── RetailBillingApplication.java   ← Entry point
│       │   ├── entity/
│       │   │   ├── Product.java                ← DB table: product
│       │   │   ├── Bill.java                   ← DB table: bill
│       │   │   └── BillItem.java               ← DB table: bill_item
│       │   ├── repository/
│       │   │   ├── ProductRepository.java      ← DB operations for Product
│       │   │   └── BillRepository.java         ← DB operations for Bill
│       │   ├── service/
│       │   │   ├── ProductService.java         ← Product business logic
│       │   │   └── BillService.java            ← Billing logic + stock update
│       │   ├── controller/
│       │   │   ├── ProductController.java      ← REST APIs for products
│       │   │   └── BillController.java         ← REST APIs for billing
│       │   ├── dto/
│       │   │   └── BillRequest.java            ← Data Transfer Object
│       │   └── exception/
│       │       ├── GlobalExceptionHandler.java ← Central error handling
│       │       ├── ResourceNotFoundException.java
│       │       └── BadRequestException.java
│       └── resources/
│           └── application.properties          ← DB config, server settings
│
├── frontend/                         ← Simple HTML Frontend
│   ├── index.html                    ← Product Management
│   ├── billing.html                  ← New Bill / Cart
│   ├── bills.html                    ← Bill History
│   ├── css/style.css                 ← All styles
│   └── js/app.js                     ← Shared utilities
│
└── docs/
    ├── schema.sql                    ← Database creation script
    └── postman-collection.json       ← API test collection
```

---

## 🏗️ Architecture Overview

```
[Browser / Frontend HTML]
        ↓ HTTP Request (JSON)
[Controller Layer]     ← Receives HTTP, validates input, delegates to Service
        ↓
[Service Layer]        ← Business logic: billing calculations, stock checks
        ↓
[Repository Layer]     ← Interfaces that talk to DB (no SQL needed!)
        ↓
[Entity / DB Layer]    ← Java classes mapped to MySQL tables via JPA
        ↓
[MySQL Database]       ← Stores all data
```

---

## 🗄️ Database Design

### Tables

| Table      | Purpose                              |
|------------|--------------------------------------|
| product    | Shop inventory (name, price, stock)  |
| bill       | Each purchase transaction            |
| bill_item  | Line items within each bill          |

### Relationships

```
product (1) ──── (*) bill_item (*) ──── (1) bill
```
- One **Bill** has many **BillItems**
- Each **BillItem** refers to one **Product**

---

## 🔄 End-to-End Project Flow

### Product Management Flow
```
User fills form → Frontend validates → POST /api/products
→ ProductController.addProduct()
→ ProductService.addProduct()
→ ProductRepository.save(product)
→ MySQL INSERT → Response JSON → Table updated
```

### Billing Flow
```
User selects products + qty → clicks "Generate Bill"
→ Frontend builds JSON payload → POST /api/bills
→ BillController.createBill()
→ BillService.createBill()
   ├─ For each item:
   │   ├─ Find product in DB
   │   ├─ Check stock availability
   │   ├─ Calculate line total + GST
   │   └─ Reduce product.quantity → save
   ├─ Calculate discount
   ├─ Compute net amount
   └─ Save Bill + BillItems (cascade)
→ MySQL INSERT (bill + bill_item rows)
→ Return Bill JSON → Invoice shown on screen
```

---

## ⚙️ How to Run the Project

### Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Any browser

### Step 1: Setup Database
```sql
-- Run in MySQL Workbench or CLI:
SOURCE docs/schema.sql;
```

### Step 2: Configure Database Connection
Edit `backend/src/main/resources/application.properties`:
```properties
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

### Step 3: Run Backend
```bash
cd backend
mvn spring-boot:run
```
Server starts at: `http://localhost:8080`

### Step 4: Open Frontend
Open any of these files in your browser:
- `frontend/index.html`   → Product Management
- `frontend/billing.html` → Generate Bills
- `frontend/bills.html`   → View Bill History

---

## 📡 API Reference

### Product APIs

| Method | URL                              | Description              |
|--------|----------------------------------|--------------------------|
| GET    | /api/products                    | Get all products         |
| GET    | /api/products/{id}               | Get product by ID        |
| POST   | /api/products                    | Add new product          |
| PUT    | /api/products/{id}               | Update product           |
| DELETE | /api/products/{id}               | Delete product           |
| GET    | /api/products/search?name=cotton | Search by name           |
| GET    | /api/products/low-stock?threshold=10 | Low stock alert      |

### Bill APIs

| Method | URL              | Description              |
|--------|------------------|--------------------------|
| POST   | /api/bills       | Create new bill          |
| GET    | /api/bills       | Get all bills            |
| GET    | /api/bills/{id}  | Get bill by ID           |

---

## 💡 Key Concepts Explained

### Why @Transactional on createBill()?
When generating a bill, multiple DB operations happen:
1. Insert into bill
2. Insert multiple bill_item rows
3. Update product quantities

If step 3 fails (e.g., DB down), without @Transactional, you'd have a bill
saved but stock not reduced — inconsistent state. @Transactional ensures
ALL operations succeed or ALL are rolled back.

### Why use DTOs (BillRequest)?
The frontend sends: `{ productId, quantity }` — not a full Bill entity.
DTOs let us accept exactly what the frontend sends, validate it, then
construct the proper entity internally in the service layer.

### Why store unitPrice in BillItem?
Product prices can change over time. By storing the price at the time
of purchase in BillItem.unitPrice, old bills always show the correct
historical price, not the current price.

---

## 🧪 Testing with Postman
1. Open Postman
2. Click Import → Upload `docs/postman-collection.json`
3. Set the `baseUrl` variable to `http://localhost:8080`
4. Run requests in order (create products first, then bills)
