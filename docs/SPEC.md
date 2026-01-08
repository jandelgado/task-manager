# Task Manager Specification

## Domain Model

### Task Entity

| Field       | Type        | Constraints                     |
|-------------|-------------|---------------------------------|
| id          | Long        | Auto-generated, Primary Key     |
| title       | String      | Required, max 100 characters    |
| description | String      | Optional, max 500 characters    |
| status      | TaskStatus  | Required, default: TODO         |
| dueDate     | LocalDate   | Optional                        |

### TaskStatus Enum

- `TODO`
- `IN_PROGRESS`
- `DONE`

## REST API

Base path: `/api/tasks`

| Method | Endpoint        | Description       | Request Body | Response      |
|--------|-----------------|-------------------|--------------|---------------|
| GET    | /               | List all tasks    | -            | Task[]        |
| GET    | /{id}           | Get task by ID    | -            | Task          |
| POST   | /               | Create task       | Task (no id) | Task          |
| PUT    | /{id}           | Update task       | Task         | Task          |
| DELETE | /{id}           | Delete task       | -            | 204 No Content|

### Validation Errors

Return 400 Bad Request with body:

```json
{
  "errors": {
    "fieldName": "error message"
  }
}
```

### Not Found

Return 404 with body:

```json
{
  "error": "Task not found"
}
```
