# E-Commerce Project

## Overview

This is a Spring Boot–based full-stack **E-Commerce Web Application** that enables users to browse products, manage their cart, and place orders. The backend is built using Java and Spring Boot, with endpoints for managing users, carts, items, and orders.

The project also demonstrates a **basic CI/CD pipeline** configured via **Jenkins**, allowing automatic build and packaging of the application through Maven.

---

##  Features

- RESTful API for Users, Items, Carts, and Orders
- Maven-based project build and dependency management
- Unit and integration tests with JaCoCo coverage support
- Dockerized build environment (in alternate setup)
- Jenkins Freestyle Job configured to:
  - Clone the project from GitHub
  - Build the application with `mvn clean package`
  - Archive the `.jar` file as build artifact

---

## Tech Stack

- **Backend:** Java 21, Spring Boot
- **Build Tool:** Maven
- **Version Control:** Git + GitHub
- **CI/CD:** Jenkins (Freestyle Job)
- **Testing:** JUnit + JaCoCo

---

## CI/CD Pipeline

The Jenkins setup automatically:

1. Clones the project repository from GitHub.
2. Builds the project using Maven (`mvn clean package`).
3. Fails gracefully if Java version is unsupported or compilation errors occur.
4. (Optional) Could be extended to run tests or deploy the `.jar` artifact.

> Screenshots showing successful Jenkins build have been included in the `E-Commerce-success-screenshots.pdf` inside the root project directory.

## Getting Started



## 📦 Build Instructions (local deployment)

```bash
# Clone the repository
git clone https://github.com/streda/E-Commerce-Project.git
cd E-Commerce-Project

# Build the project
mvn clean package