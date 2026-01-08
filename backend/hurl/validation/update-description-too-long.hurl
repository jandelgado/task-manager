# Test validation: Update task with description exceeding max length
# Description must be <= 500 characters, this tests with 501 characters

# Step 1: Create a valid task first
POST http://localhost:8080/api/tasks
Content-Type: application/json
{
  "title": "Task to test description validation",
  "description": "Short description",
  "status": "TODO"
}

HTTP 201

[Captures]
task_id: jsonpath "$.id"


# Step 2: Try to update with description that's too long (501 characters)
PUT http://localhost:8080/api/tasks/{{task_id}}
Content-Type: application/json
{
  "title": "Task to test description validation",
  "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum. Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium totam rem aperiam eaque ipsa.",
  "status": "TODO"
}

HTTP 400

[Asserts]
# Verify validation error for description field
jsonpath "$.errors" exists
jsonpath "$.errors.description" exists
