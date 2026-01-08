# Taskmanager Backend - Spring Boot Application

## Tech Stack

- Java 21
- Spring Boot 3.5+
- Spring Web, Spring Data JPA, Validation
- H2 Database (in-memory)
- Gradle with Kotlin DSL
- Lombok

## Package Structure

```
com.taskmanager
├── controller/     # REST controllers
├── service/        # Business logic
├── repository/     # Data access
├── model/          # Entity and enums
├── dto/            # Request/Response DTOs (if needed)
├── exception/      # Custom exceptions and handlers
└── config/         # Configuration classes (CORS, etc.)
```

## Conventions

- Use constructor injection (Lombok's @RequiredArgsConstructor)
- Validate inputs with Bean Validation annotations
- Use ResponseEntity for HTTP responses
- CORS enabled for http://localhost:5173

## Commands

- `./gradlew bootRun` - Run application
- `./gradlew test` - Run tests
- `./gradlew build` - Build JAR

