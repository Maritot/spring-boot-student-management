# 🎓 Student Management System — Spring Boot Interview Masterclass

A beginner-friendly, production-structured Spring Boot application designed to teach **every single fundamental Spring Boot concept** required to ace technical interviews.

---

## 📑 Table of Contents
1. [Project Overview & Architecture](#-project-overview--architecture)
2. [Quick Start Guide](#-quick-start-guide)
3. [Core Concepts Explained for Interviews](#-core-concepts-explained-for-interviews)
   - [1. Spring Framework vs Spring Boot](#1-spring-framework-vs-spring-boot)
   - [2. Inversion of Control (IoC) & Dependency Injection (DI)](#2-inversion-of-control-ioc--dependency-injection-di)
   - [3. Spring Beans, Scopes & Lifecycle](#3-spring-beans-scopes--lifecycle)
   - [4. Demystifying `@SpringBootApplication`](#4-demystifying-springbootapplication)
   - [5. How Auto-Configuration Works](#5-how-auto-configuration-works)
   - [6. Request Lifecycle (DispatcherServlet Flow)](#6-request-lifecycle-dispatcherservlet-flow)
   - [7. Layered Architecture (Controller -> Service -> Repository)](#7-layered-architecture)
   - [8. JPA vs Hibernate vs Spring Data JPA](#8-jpa-vs-hibernate-vs-spring-data-jpa)
   - [9. Entity vs DTO & Why We Never Expose Entities](#9-entity-vs-dto--why-we-never-expose-entities)
   - [10. Bean Validation API (`@Valid`, `@NotBlank`, etc.)](#10-bean-validation-api)
   - [11. Global Exception Handling (`@RestControllerAdvice`)](#11-global-exception-handling)
   - [12. Testing in Spring Boot (`@SpringBootTest` vs `@WebMvcTest`)](#12-testing-in-spring-boot)
4. [API Endpoints & Testing Guide (cURL & PowerShell)](#-api-endpoints--testing-guide)
5. [Top 20 Spring Boot Interview Questions & Answers](#-top-20-spring-boot-interview-questions--answers)

---

## 🏛 Project Overview & Architecture

This project follows the industry-standard **N-Tier (Layered) Architecture**:

```
[ Client / Browser / Postman ]
               |
         (HTTP Request)
               v
      [ DispatcherServlet ]
               |
    [ StudentController ]          <-- Presentation / REST Layer (@RestController)
               |
      [ StudentService ]           <-- Business Logic Layer (@Service, @Transactional)
               |
     [ StudentRepository ]         <-- Data Access Layer (Spring Data JPA, @Repository)
               |
    [ In-Memory H2 Database ]      <-- Persistence Layer (Table: 'students')
```

### Folder Structure
```
student-management/
├── src/
│   ├── main/
│   │   ├── java/com/example/studentmanagement/
│   │   │   ├── StudentManagementApplication.java  <-- Main Entry Point (@SpringBootApplication)
│   │   │   ├── config/
│   │   │   │   ├── AppConfig.java                 <-- @Configuration, @Bean, @Value demo
│   │   │   │   ├── BeanLifecycleDemo.java         <-- @PostConstruct, @PreDestroy demo
│   │   │   │   └── DataInitializer.java           <-- CommandLineRunner seed data
│   │   │   ├── controller/
│   │   │   │   └── StudentController.java         <-- REST Endpoints (CRUD)
│   │   │   ├── dto/
│   │   │   │   ├── ErrorResponse.java             <-- Standard JSON error payload
│   │   │   │   ├── StudentRequest.java            <-- Incoming validated request payload
│   │   │   │   └── StudentResponse.java           <-- Outgoing clean response payload
│   │   │   ├── entity/
│   │   │   │   └── Student.java                   <-- JPA Entity mapped to DB table
│   │   │   ├── exception/
│   │   │   │   ├── EmailAlreadyExistsException.java
│   │   │   │   ├── GlobalExceptionHandler.java    <-- Centralized @RestControllerAdvice
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── repository/
│   │   │   │   └── StudentRepository.java         <-- JpaRepository with custom queries
│   │   │   └── service/
│   │   │       ├── StudentService.java            <-- Service Interface
│   │   │       └── StudentServiceImpl.java        <-- Implementation with business logic
│   │   └── resources/
│   │       └── application.properties             <-- DB, H2 console, server settings
│   └── test/
│       └── java/com/example/studentmanagement/
│           ├── StudentManagementApplicationTests.java <-- @SpringBootTest context load
│           ├── controller/
│           │   └── StudentControllerTest.java     <-- @WebMvcTest slice test with MockMvc
│           └── service/
│               └── StudentServiceTest.java        <-- Pure Unit test with Mockito
├── pom.xml                                        <-- Maven dependencies
└── mvnw / mvnw.cmd                                <-- Maven Wrapper (no local Maven install needed!)
```

---

## 🚀 Quick Start Guide

### 1. Prerequisites
- **Java 21** installed (check with `java -version`)
- Maven is **NOT required** to be pre-installed! The project includes `mvnw.cmd` (Windows) and `mvnw` (Linux/macOS).

### 2. Run the Application
In your terminal, navigate to the `student-management` directory:
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Once started:
- API Base URL: `http://localhost:8080/api/v1/students`
- H2 Database Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:studentdb`
  - User Name: `sa`
  - Password: `password`

### 3. Run All Tests
```bash
.\mvnw.cmd test
```
All 13 integration, unit, and slice tests will execute and report `BUILD SUCCESS`.

---

## 💡 Core Concepts Explained for Interviews

### 1. Spring Framework vs Spring Boot

| Feature | Spring Framework | Spring Boot |
| :--- | :--- | :--- |
| **Setup & Config** | Requires heavy XML configuration or manual Java config | Convention-over-configuration with **Auto-Configuration** |
| **Server** | Needs external web server deployment (WAR file into Tomcat/WildFly) | **Embedded Tomcat/Jetty/Undertow** inside executable JAR |
| **Dependency Management** | Must specify every dependency and matching versions manually | **Starter POMs** (`spring-boot-starter-web`) handle compatible versions |
| **Production Features** | Must be coded manually | Built-in metrics, health checks, and monitoring via **Actuator** |

---

### 2. Inversion of Control (IoC) & Dependency Injection (DI)

- **What is IoC (Inversion of Control)?**
  In traditional programming, class `A` instantiates class `B` directly using `new B()`. In IoC, this control is inverted: the **Spring IoC Container** is responsible for creating, configuring, and managing the lifecycle of objects (called **Beans**).
- **What is Dependency Injection (DI)?**
  The design pattern that implements IoC. Instead of `StudentServiceImpl` creating `new StudentRepository()`, Spring injects the repository bean into the service.
- **Why Constructor Injection is Preferred Over Field Injection (`@Autowired` on field):**
  1. **Immutability:** Dependencies can be marked `final`.
  2. **Easy Unit Testing:** You can test `new StudentServiceImpl(mockRepo)` without launching Spring or using reflection.
  3. **Prevents NullPointerExceptions:** The object cannot be constructed in a partially initialized state.
  4. **Prevents Circular Dependencies:** Spring fails fast at startup if circular dependencies exist.
  5. **Clean Code:** Since Spring 4.3, if a class has a single constructor, `@Autowired` is completely optional!

---

### 3. Spring Beans, Scopes & Lifecycle

- **What is a Spring Bean?**
  A Java object instantiated, assembled, and managed by the Spring IoC Container.
- **Bean Scopes:**
  1. `singleton` (Default): Only ONE instance per Spring container. Shared across all requests.
  2. `prototype`: A NEW instance is created every time the bean is requested.
  3. `request` (Web-aware): One instance per HTTP request.
  4. `session` (Web-aware): One instance per HTTP session.
- **Bean Lifecycle Flow (Top Interview Question):**
  1. **Instantiation:** Constructor is invoked (`new Class()`).
  2. **Populate Properties:** Dependencies are injected (DI).
  3. **Aware Interfaces:** `BeanNameAware`, `BeanFactoryAware`, `ApplicationContextAware`.
  4. **BeanPostProcessor:** `postProcessBeforeInitialization()`.
  5. **`@PostConstruct`:** Initialization method executed (e.g. cache warm-up, connection validation).
  6. **`InitializingBean`:** `afterPropertiesSet()`.
  7. **Custom Init:** XML or `@Bean(initMethod = "...")`.
  8. **BeanPostProcessor:** `postProcessAfterInitialization()`.
  9. *(Bean is now in service and handling requests)*
  10. **Container Shutdown:**
      - `@PreDestroy` method executed (releasing resources).
      - `DisposableBean.destroy()`.
      - Custom destroy method.

---

### 4. Demystifying `@SpringBootApplication`

`@SpringBootApplication` is a meta-annotation that bundles three core annotations:
1. `@SpringBootConfiguration`: Marks the class as a configuration class containing `@Bean` methods.
2. `@EnableAutoConfiguration`: Scans the classpath and automatically configures beans based on detected JARs.
3. `@ComponentScan`: Scans the current package (`com.example.studentmanagement`) and all child sub-packages for `@Component`, `@Service`, `@Repository`, `@Controller`.
   > **Interview Trap:** If you put a class in a parent directory outside the main package (e.g., `com.example.service`), Spring will NOT detect it unless you explicitly define `@ComponentScan("com.example")`.

---

### 5. How Auto-Configuration Works

- Spring Boot checks classpath JARs using `AutoConfiguration.imports` (or `spring.factories` in older versions).
- It relies on **Condition Annotations**:
  - `@ConditionalOnClass`: Configures bean only if a specific class is present (e.g., if Tomcat is on classpath, configure embedded Tomcat).
  - `@ConditionalOnMissingBean`: Configures bean only if the developer hasn't defined their own custom bean.
  - `@ConditionalOnProperty`: Configures bean only if a property in `application.properties` matches.

---

### 6. Request Lifecycle (DispatcherServlet Flow)

```
[ Client Request ]
       |
       v
[ Filter Chain ] (Security, CORS, Logging)
       |
       v
[ DispatcherServlet ] (Front Controller)
       |
       +---> [ HandlerMapping ] (Finds which @RestController method matches the URL)
       |
       +---> [ HandlerAdapter ] (Executes the controller method, triggers @Valid)
       |
       +---> [ HttpMessageConverter ] (Jackson converts Java Object <--> JSON)
       |
       v
[ Client Response (JSON) ]
```

---

### 7. Layered Architecture

- **Controller (`@RestController`):** Handles HTTP requests, input validation (`@Valid`), status codes, and delegates to the Service. Contains NO business logic or SQL queries.
- **Service (`@Service`):** Implements business rules, orchestrates operations, manages transactions (`@Transactional`), and maps Entities to DTOs.
- **Repository (`@Repository`):** Communicates with the database via Spring Data JPA.

---

### 8. JPA vs Hibernate vs Spring Data JPA

- **JPA (Jakarta Persistence API):** A Java specification/standard (interfaces and annotations like `@Entity`, `@Id`). Contains NO implementation code.
- **Hibernate:** The actual ORM (Object-Relational Mapping) library that implements the JPA specification.
- **Spring Data JPA:** An abstraction layer on top of JPA/Hibernate that eliminates boilerplate DAO code by generating SQL queries dynamically from method names (`findByEmail`).

---

### 9. Entity vs DTO & Why We Never Expose Entities

- **JPA Entity (`Student`):** Mapped directly to the database table.
- **DTO (`StudentRequest` / `StudentResponse`):** Pure data carrier object designed for API requests and responses.
- **Why DTOs are mandatory:**
  1. **Security (Over-Posting Attacks):** Prevents clients from sending modified internal fields (e.g., `id`, `createdAt`, `role`).
  2. **Decoupling:** DB column renames or schema changes do not break external frontend contracts.
  3. **Circular Reference Prevention:** Directly serializing bidirectional JPA entities (`@OneToMany` / `@ManyToOne`) causes infinite JSON loops and `StackOverflowError`.

---

### 10. Bean Validation API

- `@NotNull`: Value cannot be null (allows `""` empty string).
- `@NotEmpty`: Cannot be null and size/length must be > 0 (allows `"   "` whitespace).
- `@NotBlank`: Cannot be null, and must have at least one non-whitespace character (best for strings like names, emails).
- `@Email`: Validates email pattern.
- `@Min` / `@Max`: Validates numerical ranges.

---

### 11. Global Exception Handling

- Uses `@RestControllerAdvice` + `@ExceptionHandler`.
- Intercepts exceptions thrown from any controller in the application.
- Returns a standardized JSON schema (`ErrorResponse`) with appropriate HTTP status codes (400, 404, 409, 500) rather than default stack traces or Tomcat error pages.

---

### 12. Testing in Spring Boot

- **Unit Test (with Mockito):**
  - Uses `@ExtendWith(MockitoExtension.class)`.
  - Runs in milliseconds without booting Spring.
  - `@Mock` creates mocks; `@InjectMocks` injects them into the service under test.
- **Controller Slice Test (`@WebMvcTest`):**
  - Boots ONLY the web layer (Controllers, Exception handlers, Jackson).
  - Uses `MockMvc` to perform simulated HTTP calls without starting a network port.
  - In Spring Boot 3.4+ / 4.x: Uses `@MockitoBean` (replacing deprecated `@MockBean`) to mock services.
- **Full Integration Test (`@SpringBootTest`):**
  - Boots the entire Spring container, data sources, and repositories.

---

## 📡 API Endpoints & Testing Guide

| Method | Endpoint | Description | Status Code |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/v1/students` | Get all students | `200 OK` |
| `GET` | `/api/v1/students/{id}` | Get student by ID | `200 OK` or `404 NOT FOUND` |
| `GET` | `/api/v1/students/search?course=Java` | Search students by course | `200 OK` |
| `POST` | `/api/v1/students` | Create new student | `201 CREATED` or `400 BAD REQUEST` / `409 CONFLICT` |
| `PUT` | `/api/v1/students/{id}` | Update student by ID | `200 OK` or `404 NOT FOUND` |
| `DELETE` | `/api/v1/students/{id}` | Delete student by ID | `204 NO CONTENT` or `404 NOT FOUND` |

---

### Sample Requests & Responses

#### 1. Get All Students
**cURL:**
```bash
curl -X GET http://localhost:8080/api/v1/students
```

**PowerShell:**
```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/students" -Method Get | ConvertTo-Json
```

**Response (200 OK):**
```json
[
  {
    "id": 1,
    "firstName": "Tharun",
    "lastName": "Avula",
    "fullName": "Tharun Avula",
    "email": "tharun.avula@example.com",
    "course": "Java Spring Boot",
    "age": 22,
    "createdAt": "2026-10-19T15:31:50",
    "updatedAt": "2026-10-19T15:31:50"
  }
]
```

---

#### 2. Create Student (Success)
**cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/students \
  -H "Content-Type: application/json" \
  -d '{"firstName":"David","lastName":"Miller","email":"david.miller@example.com","course":"Spring Cloud","age":25}'
```

**Response (201 Created):**
```json
{
  "id": 5,
  "firstName": "David",
  "lastName": "Miller",
  "fullName": "David Miller",
  "email": "david.miller@example.com",
  "course": "Spring Cloud",
  "age": 25,
  "createdAt": "2026-10-19T15:32:08",
  "updatedAt": "2026-10-19T15:32:08"
}
```

---

#### 3. Create Student (Validation Failure — 400 Bad Request)
**cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/students \
  -H "Content-Type: application/json" \
  -d '{"firstName":"","lastName":"","email":"invalid-email","course":"","age":12}'
```

**Response (400 Bad Request):**
```json
{
  "timestamp": "2026-10-19T15:32:15",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for one or more fields",
  "path": "/api/v1/students",
  "validationErrors": {
    "firstName": "First name must be between 2 and 50 characters",
    "lastName": "Last name is mandatory and cannot be blank",
    "email": "Email must be a valid email address format (e.g. user@example.com)",
    "course": "Course is mandatory",
    "age": "Student must be at least 16 years old"
  }
}
```

---

## 🎯 Top 20 Spring Boot Interview Questions & Answers

1. **What is the difference between `@Component`, `@Service`, and `@Repository`?**
   - `@Component`: Generic stereotype for any Spring-managed component.
   - `@Service`: Specialization of `@Component` used for the business logic layer.
   - `@Repository`: Specialization of `@Component` used for data access. It enables automatic database exception translation into Spring's `DataAccessException` hierarchy.

2. **What is the difference between `@Controller` and `@RestController`?**
   - `@Controller` is used for traditional Spring MVC web applications returning HTML views.
   - `@RestController` = `@Controller` + `@ResponseBody`. Every method serializes return values directly into JSON/XML HTTP responses.

3. **Why do we use Constructor Injection instead of `@Autowired` on fields?**
   - Enables `final` immutable fields, simplifies pure Java unit testing without Spring, prevents `NullPointerException` on incomplete objects, and fails fast on circular dependencies.

4. **What is Spring Boot Auto-Configuration and how can you disable a specific auto-configuration?**
   - It automatically registers beans based on classpath JARs and properties.
   - Disable via: `@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})` or `spring.autoconfigure.exclude` in `application.properties`.

5. **What is the purpose of `CommandLineRunner` and `ApplicationRunner`?**
   - Used to execute startup logic (like database seeding or cache warm-up) immediately after the ApplicationContext is loaded, before accepting web requests.

6. **What is the default scope of a Spring Bean?**
   - `singleton`.

7. **Is a Spring Singleton Bean thread-safe?**
   - No! By default, Spring singleton beans are shared across all incoming threads. They are only thread-safe if they are **stateless** (i.e. they do not maintain mutable instance variables).

8. **What is `@Transactional` and how does it work?**
   - It manages transaction boundaries using Spring AOP proxies. By default, it commits on success and rolls back automatically if an unchecked (`RuntimeException`) is thrown.

9. **What is the difference between `@PathVariable` and `@RequestParam`?**
   - `@PathVariable` extracts data directly from the URI path segment (`/students/{id}`).
   - `@RequestParam` extracts query string key-value parameters (`/students?course=Java`).

10. **How does Spring Boot achieve embedded servlet containers?**
    - `spring-boot-starter-web` bundles Apache Tomcat inside the application. When `SpringApplication.run()` executes, an embedded Tomcat instance is created and started programmatically.

11. **What is the difference between JPA and Hibernate?**
    - JPA is a specification (rules, interfaces). Hibernate is an implementation of JPA.

12. **What is the difference between `@NotNull`, `@NotEmpty`, and `@NotBlank`?**
    - `@NotNull`: Object != null (allows `""`).
    - `@NotEmpty`: Object != null && size > 0 (allows `"   "`).
    - `@NotBlank`: Character sequence must contain at least one non-whitespace character.

13. **What is the difference between `findById()` and `getReferenceById()` in Spring Data JPA?**
    - `findById()` hits the database immediately and returns `Optional<T>`.
    - `getReferenceById()` (formerly `getOne()`) returns a lazy proxy without hitting the database until a property is accessed.

14. **What is `@RestControllerAdvice`?**
    - A global exception handler combining `@ControllerAdvice` and `@ResponseBody` that intercepts exceptions across all controllers.

15. **What is the role of `DispatcherServlet`?**
    - The Front Controller in Spring MVC. It intercepts all incoming HTTP requests and coordinates routing them to handlers, filters, and message converters.

16. **What is the difference between `@SpringBootTest` and `@WebMvcTest`?**
    - `@SpringBootTest` loads the entire Spring application context.
    - `@WebMvcTest` is a slice test that only boots the web/controller layer, requiring service dependencies to be mocked.

17. **What is `@MockitoBean` vs `@MockBean`?**
    - In Spring Boot 3.4+ and Spring 6.2+, `@MockBean` is deprecated. `@MockitoBean` from the core Spring test framework is the modern replacement.

18. **What are Spring Profiles and how do you activate one?**
    - Segregates environment configurations (`application-dev.properties`, `application-prod.properties`). Activated via `spring.profiles.active=dev`.

19. **What is Spring Boot Actuator?**
    - A module that provides production-ready endpoints (`/actuator/health`, `/actuator/metrics`, `/actuator/env`) for monitoring application health.

20. **Why should we never expose JPA Entities in REST APIs?**
    - To prevent over-posting security vulnerabilities, prevent infinite JSON recursion from bidirectional relationships, and isolate database schema changes from client contracts.
