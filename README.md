# Devices Management API

A robust REST API designed to manage and persist device resources (e.g., smartphones, laptops, IoT sensors).

This project follows modern development and deployment practices, using a combination of Spring Boot for fast development and Docker for containerization and Java 21
# ✨ Key Features
- Device Lifecycle Management: Provides standard CRUD (Create, Read, Update, Delete) operations for device entities.

- Filtering: Supports querying devices by Brand and State.

- Validation: Includes robust input validation using Jakarta Bean Validation.

- Containerized Environment: Fully defined environment via Docker Compose for easy setup.

- API Documentation: Self-documented endpoints using SpringDoc/Swagger UI.


## 🛠️ Tech Stack

| Technology         | Purpose                                       |
|--------------------|-----------------------------------------------|
| **Java 21**        | The programming language used for all business logic.                     |
| **Spring Boot**    | Core framework for the REST API.                        |
| **PostgreSQL**     | Production-ready relational database.    |
| **Docker & Docker Compose** | Used to orchestrate the application, database, and monitoring services.   |
| **JUnit & Mockito**| Unit testing and mocking                      |
| **Bean Validation (Jakarta)** | Input validation                  |
| **Swagger / OpenAPI** | API documentation                        |
| **Prometheus**     | Metrics collection                            |
| **Grafana**        | Metrics dashboards                            |

### Prerequisites
- Java 21+
- Maven 3.9+
- Docker & Docker Compose

### 1. Clone the Repository
```bash
git clone https://github.com/RehabSayedAbdelghany/DevicesAPI.git
cd devicesApi
```
### 2. Build the Project J
```bash
mvn clean install -DskipTests
mvn clean install
```
### 3. Run with Docker Compose
```bash
# Build and start all services
docker-compose up --build

# The API will be available at:
# http://localhost:8080/device-service/v1/devices
# Swagger UI: http://localhost:8080/swagger-ui.html
```


### 3. Run Locally (without Docker)
```bash
# Start PostgreSQL (if not using Docker)
docker-compose up postgres -d

# Update application.yml with your database credentials

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### 4. Run Tests
```bash
# Run all tests with coverage
mvn clean test

# View coverage report
open target/site/jacoco/index.html

```

---

## 📚 API Documentation

### Swagger UI
Once the application is running, visit:
- **Swagger UI:** http://localhost:8080/swagger-ui.html

### Endpoints Overview

| Method | Endpoint                                  | Description              |
|--------|-------------------------------------------|--------------------------|
| POST | `/device-service/v1/devices`              | Create a new device      |
| GET | `/device-service/v1/devices`              | Get all devices          |
| GET | `/device-service/v1/devices?state=IN_USE` | Get all devices by state |
| GET | `/device-service/v1/devices?brand=brand`  | Get all devices by brand |
| GET | `/device-service/v1/devices/{id}`         | Get device by ID         |
| PATCH | `/device-service/v1/devices/{id}`         | Update device            |
| DELETE | `/device-service/v1/devices/{id}`         | Delete device            |



### Example Requests

#### Create Device
```bash
postman request POST 'http://localhost:8080/devices-api/v1/devices' \
  --header 'Content-Type: application/json' \
  --body '{
    "name": "name3",
    "brand": "brand3",
    "state": "IN_USE"
}'
```

**Response (201 Created):**
```json
{
  "id": "553ba0b5-22df-4f96-8c95-7556760a93cd",
  "name": "name3",
  "brand": "brand3",
  "state": "IN_USE",
  "creationTime": "2025-12-08T01:03:36.626992Z"
}
```

#### Get All Devices (with pagination and sorting)
```bash
curl "http://localhost:8080/devices-api/v1/devices"
```

#### Get Device by ID
```bash
curl http://localhost:8080/device-service/v1/devices/553ba0b5-22df-4f96-8c95-7556760a93cd
```

#### Get Devices by Brand
```bash
curl "http://localhost:8080/device-service/v1/devices?brand=brand3"
```

#### Get Devices by State
```bash
curl "http://localhost:8080/device-service/v1/devices?state=IN_USE"
```

#### Update
```bash
# Update only state
postman request PUT 'http://localhost:8080/devices-api/v1/devices/bd90c21e-9f71-446d-9fef-22e93aa35c41' \
  --header 'Content-Type: application/json' \
  --body '{
    "name": "name",
    "brand": "brand",
    "state": "IN_USE"
}'

# Update name and brand (only when device is not IN_USE)
postman request PATCH 'http://localhost:8080/devices-api/v1/devices/6e65a3b1-e42b-4b1a-9982-de575c2cd98c' \
  --header 'Content-Type: application/json' \
  --body '{
    "name": "rehab"
}'
```

#### Delete Device
```bash
curl -X DELETE http://localhost:8080/device-service/v1/devices/6e65a3b1-e42b-4b1a-9982-de575c2cd98c
```

---


## 🧪 Testing & Code Coverage

you can run the test only and also see the coverage

### JaCoCo Coverage Report

After running tests, open the interactive coverage report:

```bash
mvn clean test
open target/site/jacoco/index.html
```

## 🐳 Docker Configuration

### docker-compose.yml

The project includes a complete Docker Compose setup with:

**Services:**
1. **PostgreSQL Database**
    - Image: `postgres:16-alpine`
    - Port: `5432:5432`
    - Health checks enabled
    - Persistent data storage

2. **Spring Boot Application**
    - Built from local Dockerfile
    - Port: `8080:8080`
    - Depends on PostgreSQL health check
    - Auto-restart enabled
3. **Prometheus**
    - Port: `9090:9090`
    - Time-series database responsible for collecting metrics (scrapes the Spring Boot Actuator endpoint).

3. **Grafana**
    - Port: `3000:3000`
    - TVisualization platform used to display metrics collected by Prometheus, providing dashboards for system health

**Docker Commands:**

```bash
# Start all services
docker-compose up --build

# Start in background
docker-compose up -d

# View logs
docker-compose logs -f app

# Stop services
docker-compose down

# Stop and remove volumes (clean database)
docker-compose down -v

# Restart a specific service
docker-compose restart app
```

## 📊 Monitoring – Prometheus & Grafana

Spring Boot exposes metrics via /actuator/prometheus 

Prometheus scrapes metrics every few seconds

Grafana provides real-time dashboards and system insights

Prometheus	http://localhost:9090

Grafana	http://localhost:3000