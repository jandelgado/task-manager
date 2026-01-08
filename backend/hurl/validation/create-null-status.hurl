# Test validation: Create task with null status
# Status field is required (@NotNull), so this should return 400 Bad Request

POST http://localhost:8080/api/tasks
Content-Type: application/json
{
  "title": "Task with null status",
  "description": "Testing status validation",
  "status": null
}

HTTP 400

[Asserts]
# Verify validation error for status field
jsonpath "$.errors" exists
jsonpath "$.errors.status" exists
