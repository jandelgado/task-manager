# Task Manager - Just Commands
# Run 'just' or 'just --list' to see all available commands

# List available commands
default:
    @just --list

# Install all dependencies
install:
    cd backend && ./gradlew build --no-daemon
    cd frontend && npm install

# Start backend server
backend:
    cd backend && ./gradlew bootRun

# Start frontend dev server
frontend:
    cd frontend && npm run dev

# Run both servers (requires terminal multiplexer or separate terminals)
dev:
    @echo "Start backend in one terminal: just backend"
    @echo "Start frontend in another terminal: just frontend"

# Run all backend tests
test-backend:
    cd backend && ./gradlew test

# Run backend tests with report
test-backend-report:
    cd backend && ./gradlew test
    @echo "Opening test report..."
    @if [ "$(uname)" = "Darwin" ]; then open backend/build/reports/tests/test/index.html; else xdg-open backend/build/reports/tests/test/index.html 2>/dev/null || echo "Open backend/build/reports/tests/test/index.html in your browser"; fi

# Run all Hurl API tests (requires backend running)
test-api:
    cd backend/hurl && hurl 0*.hurl validation/*.hurl errors/*.hurl

# Run specific Hurl test file
test-api-file file:
    cd backend/hurl && hurl {{file}}

# Build backend JAR
build-backend:
    cd backend && ./gradlew bootJar

# Build frontend for production
build-frontend:
    cd frontend && npm run build

# Build everything
build: build-backend build-frontend

# Clean all build artifacts
clean:
    cd backend && ./gradlew clean
    rm -rf frontend/dist frontend/node_modules

# Clean backend only
clean-backend:
    cd backend && ./gradlew clean

# Clean frontend only
clean-frontend:
    rm -rf frontend/dist frontend/node_modules

# Build Docker image for backend
docker-build:
    cd backend && docker build -t taskmanager-api .

# Run Docker container locally
docker-run:
    docker run -p 8080:8080 taskmanager-api

# Build and run Docker container
docker: docker-build docker-run

# Deploy backend to Fly.io
deploy-backend:
    cd backend && fly deploy

# Deploy frontend to Vercel
deploy-frontend:
    cd frontend && vercel --prod

# Lint frontend code
lint-frontend:
    cd frontend && npm run lint

# Format frontend code
format-frontend:
    cd frontend && npm run format

# Check H2 database console URL
db-console:
    @echo "H2 Console: http://localhost:8080/h2-console"
    @echo "JDBC URL: jdbc:h2:mem:taskdb"
    @echo "Username: sa"
    @echo "Password: (leave empty)"

# Show project status
status:
    @echo "=== Task Manager Project Status ==="
    @echo ""
    @echo "Backend:"
    @cd backend && ./gradlew -version | head -n 1
    @echo ""
    @echo "Frontend:"
    @cd frontend && node --version && npm --version
    @echo ""
    @echo "Development tools:"
    @hurl --version
    @just --version
