# Complete CRUD Workflow
# This demonstrates a full lifecycle: Create → Read → Update → Delete
# All operations are chained together using variable capture

# Step 1: Create a new task
POST http://localhost:8080/api/tasks
Content-Type: application/json
{
  "title": "Workflow test task",
  "description": "Testing complete CRUD workflow",
  "status": "TODO",
  "dueDate": "2026-02-28"
}

HTTP 201

[Captures]
task_id: jsonpath "$.id"

[Asserts]
jsonpath "$.id" exists
jsonpath "$.title" == "Workflow test task"
jsonpath "$.status" == "TODO"


# Step 2: Get all tasks (should include our new task)
GET http://localhost:8080/api/tasks

HTTP 200

[Asserts]
jsonpath "$" isCollection
jsonpath "$" count >= 1


# Step 3: Get the specific task by ID
GET http://localhost:8080/api/tasks/{{task_id}}

HTTP 200

[Asserts]
jsonpath "$.id" == {{task_id}}
jsonpath "$.title" == "Workflow test task"
jsonpath "$.description" == "Testing complete CRUD workflow"
jsonpath "$.status" == "TODO"


# Step 4: Update the task to IN_PROGRESS
PUT http://localhost:8080/api/tasks/{{task_id}}
Content-Type: application/json
{
  "title": "Workflow test task (Updated)",
  "description": "Updated during CRUD workflow test",
  "status": "IN_PROGRESS",
  "dueDate": "2026-03-15"
}

HTTP 200

[Asserts]
jsonpath "$.id" == {{task_id}}
jsonpath "$.title" == "Workflow test task (Updated)"
jsonpath "$.status" == "IN_PROGRESS"
jsonpath "$.dueDate" == "2026-03-15"


# Step 5: Get the updated task to verify changes persisted
GET http://localhost:8080/api/tasks/{{task_id}}

HTTP 200

[Asserts]
jsonpath "$.title" == "Workflow test task (Updated)"
jsonpath "$.description" == "Updated during CRUD workflow test"
jsonpath "$.status" == "IN_PROGRESS"


# Step 6: Mark task as DONE
PUT http://localhost:8080/api/tasks/{{task_id}}
Content-Type: application/json
{
  "title": "Workflow test task (Completed)",
  "description": "Finished CRUD workflow test",
  "status": "DONE",
  "dueDate": "2026-03-15"
}

HTTP 200

[Asserts]
jsonpath "$.status" == "DONE"


# Step 7: Delete the task
DELETE http://localhost:8080/api/tasks/{{task_id}}

HTTP 204


# Step 8: Verify the task is deleted (should return 404)
GET http://localhost:8080/api/tasks/{{task_id}}

HTTP 404

[Asserts]
jsonpath "$.error" == "Task not found"
