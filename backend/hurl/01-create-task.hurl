# Create a new task with all fields populated
# This test creates a task and verifies the response contains the correct data

POST http://localhost:8080/api/tasks
Content-Type: application/json
{
  "title": "Implement feature X",
  "description": "Add new functionality to the application",
  "status": "TODO",
  "dueDate": "2026-02-15"
}

HTTP 201

[Asserts]
# Verify the response contains an auto-generated ID
jsonpath "$.id" exists
jsonpath "$.id" isInteger

# Verify all fields are returned correctly
jsonpath "$.title" == "Implement feature X"
jsonpath "$.description" == "Add new functionality to the application"
jsonpath "$.status" == "TODO"
jsonpath "$.dueDate" == "2026-02-15"
