# Test error handling: Get a non-existent task
# Using a very high ID that likely doesn't exist should return 404 Not Found

GET http://localhost:8080/api/tasks/999999

HTTP 404

[Asserts]
# Verify the error response format
jsonpath "$.error" exists
jsonpath "$.error" == "Task not found"
