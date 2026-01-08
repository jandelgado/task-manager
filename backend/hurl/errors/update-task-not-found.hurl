# Test error handling: Update a non-existent task
# Attempting to update a task that doesn't exist should return 404 Not Found

PUT http://localhost:8080/api/tasks/999999
Content-Type: application/json
{
  "title": "Trying to update non-existent task",
  "description": "This should fail with 404",
  "status": "TODO"
}

HTTP 404

[Asserts]
# Verify the error response format
jsonpath "$.error" exists
jsonpath "$.error" == "Task not found"
