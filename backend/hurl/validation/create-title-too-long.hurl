# Test validation: Create task with title exceeding max length
# Title must be <= 100 characters, this tests with 101 characters

POST http://localhost:8080/api/tasks
Content-Type: application/json
{
  "title": "This is a very long title that exceeds the maximum allowed length of 100 characters for the title field",
  "description": "Testing title length validation",
  "status": "TODO"
}

HTTP 400

[Asserts]
# Verify validation error for title field
jsonpath "$.errors" exists
jsonpath "$.errors.title" exists
