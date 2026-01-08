# Test validation: Create task with blank title
# Title field is required (@NotBlank), so this should return 400 Bad Request

POST http://localhost:8080/api/tasks
Content-Type: application/json
{
  "title": "",
  "description": "Task with invalid title",
  "status": "TODO"
}

HTTP 400

[Asserts]
# Verify validation error response format
jsonpath "$.errors" exists
jsonpath "$.errors.title" exists
