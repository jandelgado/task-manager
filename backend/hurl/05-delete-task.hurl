# Delete a task
# This test creates a task, deletes it, then verifies it's gone

# Step 1: Create a task to delete
POST http://localhost:8080/api/tasks
Content-Type: application/json
{
  "title": "Task to delete",
  "description": "This task will be deleted",
  "status": "TODO"
}

HTTP 201

[Captures]
task_id: jsonpath "$.id"

[Asserts]
jsonpath "$.id" exists


# Step 2: Delete the task
DELETE http://localhost:8080/api/tasks/{{task_id}}

HTTP 204


# Step 3: Verify the task is deleted (should return 404)
GET http://localhost:8080/api/tasks/{{task_id}}

HTTP 404

[Asserts]
jsonpath "$.error" == "Task not found"
