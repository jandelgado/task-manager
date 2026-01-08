# Hurl Manual API Testing

This directory contains Hurl files for manually testing the Task Manager REST API. Hurl is a command-line tool that runs HTTP requests defined in plain text files, making it perfect for manual API testing and documentation.

## What is Hurl?

[Hurl](https://hurl.dev) is a command-line tool that allows you to run and test HTTP requests with plain text. It's like curl but with better testing capabilities:

- Run HTTP requests defined in simple text files
- Test HTTP responses (status codes, headers, JSON body)
- Chain requests together (use response from one request in the next)
- Beautiful colored output
- Works as both a testing tool and executable documentation

## Installation

### Using Devbox (Recommended)

Hurl is already configured in `devbox.json`. Just enter the devbox shell:

```bash
devbox shell
hurl --version
```

### Manual Installation

Alternatively, install Hurl manually:

- **macOS**: `brew install hurl`
- **Linux**: Download from https://hurl.dev/docs/installation.html
- **Windows**: Download from https://hurl.dev/docs/installation.html

## Prerequisites

**The backend must be running** on http://localhost:8080 before running any Hurl tests.

Start the backend:
```bash
cd backend
devbox shell
./gradlew bootRun
```

## Directory Structure

```
hurl/
├── README.md                          # This file
├── 01-create-task.hurl                # Create a new task
├── 02-get-all-tasks.hurl              # List all tasks
├── 03-get-task-by-id.hurl             # Get a specific task
├── 04-update-task.hurl                # Update a task
├── 05-delete-task.hurl                # Delete a task
├── 06-full-crud-workflow.hurl         # Complete CRUD workflow
├── validation/                        # Validation error tests
│   ├── create-invalid-title.hurl
│   ├── create-title-too-long.hurl
│   ├── create-null-status.hurl
│   └── update-description-too-long.hurl
└── errors/                            # Error handling tests
    ├── get-task-not-found.hurl
    ├── update-task-not-found.hurl
    └── delete-task-not-found.hurl
```

## Running Hurl Files

### Run a Single File

```bash
cd backend/hurl
hurl 01-create-task.hurl
```

**Successful output:**
```
✓ POST http://localhost:8080/api/tasks
  HTTP 201
  [Asserts]
    ✓ jsonpath "$.id" exists
    ✓ jsonpath "$.title" == "Implement feature X"
```

### Run with Verbose Output

See full request and response details:

```bash
hurl --verbose 01-create-task.hurl
```

### Run All Basic CRUD Tests

```bash
cd backend/hurl
for file in 0*.hurl; do
  echo "Running $file..."
  hurl "$file" && echo "✓ Passed" || echo "✗ Failed"
done
```

### Run All Validation Tests

```bash
cd backend/hurl/validation
hurl *.hurl
```

### Run All Error Tests

```bash
cd backend/hurl/errors
hurl *.hurl
```

### Run All Tests

```bash
cd backend/hurl
hurl 0*.hurl validation/*.hurl errors/*.hurl
```

## Test Files Overview

### Basic CRUD Operations

| File | Description | Method | Endpoint |
|------|-------------|--------|----------|
| `01-create-task.hurl` | Create a new task | POST | `/api/tasks` |
| `02-get-all-tasks.hurl` | List all tasks | GET | `/api/tasks` |
| `03-get-task-by-id.hurl` | Get specific task | GET | `/api/tasks/{id}` |
| `04-update-task.hurl` | Update a task | PUT | `/api/tasks/{id}` |
| `05-delete-task.hurl` | Delete a task | DELETE | `/api/tasks/{id}` |
| `06-full-crud-workflow.hurl` | Complete CRUD flow | Multiple | Multiple |

### Validation Tests

| File | Test Case | Expected |
|------|-----------|----------|
| `validation/create-invalid-title.hurl` | Blank title | 400 Bad Request |
| `validation/create-title-too-long.hurl` | Title > 100 chars | 400 Bad Request |
| `validation/create-null-status.hurl` | Null status | 400 Bad Request |
| `validation/update-description-too-long.hurl` | Description > 500 chars | 400 Bad Request |

### Error Handling Tests

| File | Test Case | Expected |
|------|-----------|----------|
| `errors/get-task-not-found.hurl` | GET non-existent task | 404 Not Found |
| `errors/update-task-not-found.hurl` | UPDATE non-existent task | 404 Not Found |
| `errors/delete-task-not-found.hurl` | DELETE non-existent task | 404 Not Found |

## Hurl File Format Examples

### Simple GET Request

```hurl
# Get all tasks
GET http://localhost:8080/api/tasks
HTTP 200
[Asserts]
jsonpath "$" isCollection
```

### POST Request with JSON Body

```hurl
# Create a task
POST http://localhost:8080/api/tasks
Content-Type: application/json
{
  "title": "My Task",
  "description": "Task description",
  "status": "TODO",
  "dueDate": "2026-02-15"
}
HTTP 201
[Asserts]
jsonpath "$.id" exists
jsonpath "$.title" == "My Task"
```

### Chaining Requests (Capture & Reuse)

```hurl
# Step 1: Create a task and capture its ID
POST http://localhost:8080/api/tasks
Content-Type: application/json
{"title": "Task", "status": "TODO"}
HTTP 201
[Captures]
task_id: jsonpath "$.id"

# Step 2: Get the created task using captured ID
GET http://localhost:8080/api/tasks/{{task_id}}
HTTP 200
[Asserts]
jsonpath "$.id" == {{task_id}}
jsonpath "$.title" == "Task"
```

## Common Assertions

### Status Code Assertions

```hurl
HTTP 200  # OK
HTTP 201  # Created
HTTP 204  # No Content
HTTP 400  # Bad Request
HTTP 404  # Not Found
```

### JSON Path Assertions

```hurl
[Asserts]
# Check field exists
jsonpath "$.id" exists

# Check exact value
jsonpath "$.title" == "My Task"

# Check value matches pattern
jsonpath "$.status" matches "TODO|IN_PROGRESS|DONE"

# Check array length
jsonpath "$" count > 0

# Check nested error field
jsonpath "$.errors.title" exists
jsonpath "$.error" == "Task not found"
```

## Troubleshooting

### Backend Not Running

**Error:** `Connection refused`

**Solution:** Start the backend first:
```bash
cd backend
devbox shell
./gradlew bootRun
```

### Hurl Not Found

**Error:** `hurl: command not found`

**Solution:** Enter devbox shell or install Hurl manually:
```bash
devbox shell
```

### Test Fails with 404

If CRUD tests fail with 404, the database might be empty. Run `01-create-task.hurl` first to populate data.

### JSON Assertion Fails

Make sure the backend is returning valid JSON. Check the response with verbose mode:
```bash
hurl --verbose 01-create-task.hurl
```

## Tips

1. **Run tests in order** - Files 01-06 are numbered for sequential execution
2. **Check verbose output** - Use `--verbose` to see full request/response
3. **Use variables** - Hurl supports variables for base URL customization
4. **Fresh database** - For consistent tests, restart the backend (it uses in-memory H2)
5. **Chain requests** - Files 03-05 demonstrate request chaining with captured variables

## Advanced Usage

### Override Base URL

```bash
hurl --variable base_url=http://other:8080 01-create-task.hurl
```

### Generate HTML Report

```bash
hurl --report-html report/ *.hurl
```

### Run in Test Mode

```bash
hurl --test *.hurl
```

## Benefits

- **Quick Manual Testing:** Test endpoints without writing code
- **Executable Documentation:** Hurl files document your API by example
- **Regression Testing:** Run all files to verify API still works after changes
- **CI/CD Integration:** Can be integrated into continuous integration pipelines
- **Version Control:** Plain text files work great with git
- **Share with Team:** Easy to share and understand

## Learn More

- Hurl Documentation: https://hurl.dev/docs
- JSONPath Syntax: https://goessner.net/articles/JsonPath/
- Task Manager API Spec: `../docs/SPEC.md`

## Contributing

When adding new Hurl files:
1. Follow the naming convention (`##-description.hurl`)
2. Add descriptive comments
3. Include meaningful assertions
4. Update this README with the new file
5. Test with a running backend
