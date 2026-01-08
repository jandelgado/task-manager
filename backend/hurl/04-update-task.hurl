# Update an existing task
# This test creates a task, then updates its fields

# Step 1: Create a task to update
POST http://localhost:8080/api/tasks
Content-Type: application/json
{
  "title": "Original title",
  "description": "Original description",
  "status": "TODO",
  "dueDate": "2026-02-15"
}

HTTP 201

[Captures]
task_id: jsonpath "$.id"

[Asserts]
jsonpath "$.title" == "Original title"


# Step 2: Update the task with new values
PUT http://localhost:8080/api/tasks/{{task_id}}
Content-Type: application/json
{
  "title": "Updated title",
  "description": "Updated description",
  "status": "IN_PROGRESS",
  "dueDate": "2026-03-01"
}

HTTP 200

[Asserts]
# Verify all fields were updated
jsonpath "$.id" == {{task_id}}
jsonpath "$.title" == "Updated title"
jsonpath "$.description" == "Updated description"
jsonpath "$.status" == "IN_PROGRESS"
jsonpath "$.dueDate" == "2026-03-01"
