# Get a specific task by ID
# This test first creates a task, captures its ID, then retrieves it

# Step 1: Create a task to ensure we have one to retrieve
POST http://localhost:8080/api/tasks
Content-Type: application/json
{
  "title": "Task to retrieve",
  "description": "This task will be retrieved by ID",
  "status": "TODO",
  "dueDate": "2026-02-20"
}

HTTP 201

[Captures]
# Capture the ID from the created task
task_id: jsonpath "$.id"

[Asserts]
jsonpath "$.id" exists


# Step 2: Get the task by its ID
GET http://localhost:8080/api/tasks/{{task_id}}

HTTP 200

[Asserts]
# Verify the returned task matches what we created
jsonpath "$.id" == {{task_id}}
jsonpath "$.title" == "Task to retrieve"
jsonpath "$.description" == "This task will be retrieved by ID"
jsonpath "$.status" == "TODO"
jsonpath "$.dueDate" == "2026-02-20"
