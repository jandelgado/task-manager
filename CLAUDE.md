# Task Manager Project

A full-stack task management application.

## Project Structure

- `/frontend` - React + TypeScript + Vite application
- `/backend` - Spring Boot + Java 21 + Gradle application
- `/docs` - Specifications and documentation

## Getting Started

### Backend

The backend uses Devbox to manage Java dependencies. First, enter the Devbox shell:

```bash
cd backend && devbox shell
./gradlew bootRun
```
Runs on http://localhost:8080

### Frontend

```bash
cd frontend && npm run dev
```
Runs on http://localhost:5173

## Development Notes

- H2 Console: http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:taskdb)
- API Base URL: http://localhost:8080/api
- Frontend proxies /api requests to backend in development


