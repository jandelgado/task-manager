# Test error handling: Delete a non-existent task
# Attempting to delete a task that doesn't exist should return 404 Not Found

DELETE http://localhost:8080/api/tasks/999999

HTTP 404

[Asserts]
# Verify the error response format
jsonpath "$.error" exists
jsonpath "$.error" == "Task not found"
