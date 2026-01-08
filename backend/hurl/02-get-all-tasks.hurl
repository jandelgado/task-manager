# Get all tasks from the system
# This returns an array of all tasks (may be empty if no tasks exist)

GET http://localhost:8080/api/tasks

HTTP 200

[Asserts]
# Verify the response is a JSON array
jsonpath "$" isCollection

# The array may be empty or contain tasks
# If you want to ensure tasks exist, run 01-create-task.hurl first
