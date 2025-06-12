# Order Processing System

## Overview

This is a complex Java-based Order Processing System designed to simulate real-world e-commerce order management. The system includes multiple layers such as domain models, repositories, services, utilities, and exception handling. It supports order placement, inventory management, discount calculation, and payment processing.

---

## Features Implemented So Far

- **Domain Models**:
  - `Order`, `Product`, and `OrderStatus` classes representing core entities.
  - Orders contain multiple products, track total price, discounts, and status.

- **Repository Layer**:
  - Thread-safe in-memory repositories (`OrderRepository`, `InventoryRepository`) using `ConcurrentHashMap` for storing orders and inventory data.

- **Service Layer**:
  - `OrderService`: Manages order placement, including inventory checks, discount application, payment processing, and order status updates.
  - `InventoryService`: Checks product availability and manages inventory reduction with proper concurrency control.
  - `PaymentService`: Simulates payment processing.

- **Utility Layer**:
  - `DiscountCalculator`: Calculates discounts based on product quantities with fixed logic (10% discount for products with quantity > 5).

- **Exception Handling**:
  - Custom `InsufficientInventoryException` to handle inventory shortage scenarios.

- **Concurrency and Atomicity**:
  - Atomic inventory check and reduction to prevent race conditions and partial order processing.
  - Synchronized methods and thread-safe data structures used for inventory management.

- **Unit Testing**:
  - Comprehensive JUnit 5 tests covering successful order placement, insufficient inventory handling, and discount correctness.
  - Tests updated to reflect fixed business logic and verify side effects like inventory reduction.

- **Build and CI Setup**:
  - Maven project configured with Java 11 and JUnit 5.
  - GitHub Actions workflow file provided to automate build, test, and package steps on push or pull request to the main branch.

---

## How to Build and Run Tests

1. **Prerequisites**:
  - Java JDK 11 or higher installed.
  - Apache Maven installed.

2. **Build the project**: `mvn clean compile`
3. **Run unit tests**: `mvn test`
4. **Package the application** (optional): `mvn package`


---

## GitHub Actions CI Workflow

- Automatically triggered on push or pull requests to `main` branch.
- Steps include checkout, JDK setup, Maven build, test, and package.
- Caches Maven dependencies for faster builds.

---

## Known Bugs and Fixes Applied

- Fixed discount calculation to correctly multiply price by quantity.
- Corrected inventory availability check to use `>=` operator.
- Added atomic inventory check and reduction to prevent race conditions.
- Ensured inventory is reduced only after successful payment.
- Updated unit tests to verify fixed logic and side effects.

---
## Contact

For questions or contributions, please contact the project maintainer.

---

Thank you for exploring the Order Processing System project!