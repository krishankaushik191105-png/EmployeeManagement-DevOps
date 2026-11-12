# Employee Management REST API

A beginner-friendly RESTful API built with **Java 17**, **Spring Boot 3.2**, and **Gradle**.  
No database required — all data is stored in memory using a `ConcurrentHashMap`.

---

## Table of Contents

- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Sample Requests](#sample-requests)
- [Running Tests](#running-tests)
- [Error Handling](#error-handling)
- [Git Branching and Version Control](#git-branching-and-version-control)

---

## Project Structure

```
employee/
├── build.gradle                          ← Gradle build configuration
├── settings.gradle                       ← Project name
├── gradle/wrapper/
│   └── gradle-wrapper.properties         ← Gradle version pinned to 8.5
└── src/
    ├── main/
    │   ├── java/com/example/employee/
    │   │   ├── EmployeeManagementApplication.java   ← Spring Boot entry point
    │   │   ├── controller/
    │   │   │   └── EmployeeController.java           ← REST endpoints
    │   │   ├── service/
    │   │   │   └── EmployeeService.java              ← Business logic
    │   │   ├── repository/
    │   │   │   └── EmployeeRepository.java           ← In-memory data store
    │   │   ├── model/
    │   │   │   └── Employee.java                     ← Data model (Java record)
    │   │   └── exception/
    │   │       ├── EmployeeNotFoundException.java    ← Custom exception
    │   │       ├── ErrorResponse.java                ← Error response format
    │   │       └── GlobalExceptionHandler.java       ← Centralized error handling
    │   └── resources/
    │       └── application.properties               ← Server config (port 8080)
    └── test/
        └── java/com/example/employee/
            ├── EmployeeManagementApplicationTest.java  ← Context smoke test
            ├── controller/
            │   └── EmployeeControllerTest.java         ← MockMvc controller tests
            ├── service/
            │   └── EmployeeServiceTest.java            ← Mockito service tests
            └── repository/
                └── EmployeeRepositoryTest.java         ← Pure unit repository tests
```

---

## Prerequisites

| Tool | Minimum Version | Download |
|------|----------------|---------|
| Java JDK | 17 | https://adoptium.net |
| Gradle (via wrapper) | 8.5 (auto-downloaded) | *(no install needed)* |

> **Tip:** You do NOT need to install Gradle separately.  
> The `gradlew` / `gradlew.bat` wrapper script downloads Gradle automatically on first run.

---

## Running the Application

### On Windows (Command Prompt / PowerShell)

```powershell
# Navigate to the project directory
cd path\to\employee

# Download Gradle wrapper JAR first (one-time setup)
# If gradlew.bat is missing, run:
gradle wrapper

# Build and run
.\gradlew.bat bootRun
```

### On macOS / Linux

```bash
cd path/to/employee
chmod +x gradlew
./gradlew bootRun
```

Once started, the API is available at: **http://localhost:8080**

---

## API Endpoints

| Method | Endpoint | Description | Success Status |
|--------|----------|-------------|---------------|
| `GET` | `/employees` | Get all employees | `200 OK` |
| `GET` | `/employees/{id}` | Get employee by ID | `200 OK` |
| `POST` | `/employees` | Add a new employee | `201 Created` |

### Employee JSON Schema

```json
{
  "id":         1,
  "firstName":  "Alice",
  "lastName":   "Johnson",
  "email":      "alice@example.com",
  "department": "Engineering",
  "salary":     85000.0
}
```

> When **creating** an employee (`POST`), omit the `id` field — it is auto-assigned by the server.

---

## Sample Requests

### GET all employees

```bash
curl -X GET http://localhost:8080/employees
```

**Response `200 OK`:**
```json
[
  { "id": 1, "firstName": "Alice",   "lastName": "Johnson", "email": "alice@example.com",   "department": "Engineering", "salary": 85000.0 },
  { "id": 2, "firstName": "Bob",     "lastName": "Smith",   "email": "bob@example.com",     "department": "Marketing",   "salary": 72000.0 },
  { "id": 3, "firstName": "Charlie", "lastName": "Brown",   "email": "charlie@example.com", "department": "Finance",     "salary": 91000.0 }
]
```

---

### GET employee by ID

```bash
curl -X GET http://localhost:8080/employees/1
```

**Response `200 OK`:**
```json
{ "id": 1, "firstName": "Alice", "lastName": "Johnson", "email": "alice@example.com", "department": "Engineering", "salary": 85000.0 }
```

**Response `404 Not Found` (ID doesn't exist):**
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status":    404,
  "error":     "Not Found",
  "message":   "Employee not found with id: 999",
  "path":      "/employees/999",
  "details":   []
}
```

---

### POST — Add a new employee

```bash
curl -X POST http://localhost:8080/employees \
  -H "Content-Type: application/json" \
  -d '{
    "firstName":  "Jane",
    "lastName":   "Doe",
    "email":      "jane.doe@example.com",
    "department": "HR",
    "salary":     75000.0
  }'
```

**Response `201 Created`:**
```json
{ "id": 4, "firstName": "Jane", "lastName": "Doe", "email": "jane.doe@example.com", "department": "HR", "salary": 75000.0 }
```

**Response `400 Bad Request` (validation failure):**
```json
{
  "timestamp": "2024-01-15T10:35:00",
  "status":    400,
  "error":     "Bad Request",
  "message":   "Validation failed. Please check the request body.",
  "path":      "/employees",
  "details": [
    "firstName: First name is required",
    "email: Email must be a valid email address"
  ]
}
```

---

## Running Tests

```powershell
# Run all tests (Windows)
.\gradlew.bat test

# Run all tests (macOS/Linux)
./gradlew test

# View HTML test report (opens in browser)
# Report is generated at:
# build/reports/tests/test/index.html
```

### Test Suite Overview

| Test Class | Type | What It Tests |
|-----------|------|--------------|
| `EmployeeManagementApplicationTest` | Smoke | Full Spring context starts |
| `EmployeeControllerTest` | MockMvc | HTTP layer: status codes, JSON responses, validation |
| `EmployeeServiceTest` | Unit (Mockito) | Business logic, exception throwing |
| `EmployeeRepositoryTest` | Unit | In-memory CRUD operations |

---

## Error Handling

All errors follow a consistent JSON structure:

| Field | Description |
|-------|-------------|
| `timestamp` | When the error occurred |
| `status` | HTTP status code (e.g., 404, 400, 500) |
| `error` | HTTP status name (e.g., "Not Found") |
| `message` | Human-readable explanation |
| `path` | The URL path that caused the error |
| `details` | List of field-level validation messages (empty for non-validation errors) |

| Scenario | HTTP Status |
|---------|------------|
| Employee ID not found | `404 Not Found` |
| Invalid request body (validation) | `400 Bad Request` |
| Unexpected server error | `500 Internal Server Error` |

---

## Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 17 | Programming language |
| Spring Boot | 3.2.0 | Web framework |
| Spring Web | (via Boot) | REST API layer |
| Spring Validation | (via Boot) | Bean Validation (JSR-380) |
| Gradle | 8.5 | Build tool |
| JUnit 5 | (via Boot) | Unit & integration testing |
| Mockito | (via Boot) | Mocking in unit tests |
| AssertJ | (via Boot) | Fluent test assertions |
| MockMvc | (via Boot) | HTTP layer testing |

---

## Git Branching and Version Control

This project follows a simple, beginner-friendly Git branching strategy to keep development organised and the `main` branch stable at all times.

### Branch Structure

| Branch | Purpose |
|--------|---------|
| `main` | The stable, production-ready branch. Only contains fully tested and reviewed code. |
| `feature/<name>` | Short-lived branches created for each new feature or bug fix. |

### Workflow

1. **Start from `main`** — always branch off the latest `main` to ensure your feature starts from a clean, up-to-date baseline.

   ```bash
   git checkout main
   git pull origin main
   ```

2. **Create a feature branch** — use a descriptive name that reflects what the branch does.

   ```bash
   # Examples
   git checkout -b feature/add-department-search
   git checkout -b feature/delete-employee
   git checkout -b fix/validation-error-message
   ```

3. **Develop and commit** — make small, focused commits with clear messages.

   ```bash
   git add .
   git commit -m "feat: add GET /employees/department/{department} endpoint"
   ```

4. **Merge into `main`** — once development is complete and all tests pass, merge the feature branch back into `main`.

   ```bash
   git checkout main
   git merge feature/add-department-search
   git push origin main
   ```

5. **Delete the feature branch** — clean up after merging to keep the repository tidy.

   ```bash
   git branch -d feature/add-department-search
   ```

### Commit Message Convention

Use the following prefixes to make the project history easy to read:

| Prefix | When to use |
|--------|-------------|
| `feat:` | Adding a new feature |
| `fix:` | Fixing a bug |
| `test:` | Adding or updating tests |
| `docs:` | Documentation changes only |
| `refactor:` | Code restructuring without behaviour change |

> **Tip:** Never commit directly to `main`. Always develop on a feature branch and merge once your changes are tested and reviewed.
