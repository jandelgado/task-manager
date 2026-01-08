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

## Development Environment

This project uses [Devbox](https://www.jetify.com/devbox) to manage Java and other dependencies.

### Setup

1. Install Devbox (if not already installed):
   ```bash
   curl -fsSL https://get.jetify.com/devbox | bash
   ```

2. Initialize the Devbox environment:
   ```bash
   devbox shell
   ```

The `devbox.json` file configures Java 21 automatically.

## Commands

All commands should be run inside the Devbox shell (`devbox shell`):

- `./gradlew bootRun` - Run application
- `./gradlew test` - Run tests
- `./gradlew build` - Build JAR

