# Experimenting with claude code: The Task Manager

A full-stack task management application built with React, TypeScript, Vite, Spring Boot, and Java.

## Overview

Task Manager is a modern web application that allows users to create, update, and manage tasks with different statuses. The application features a responsive UI with dark mode support and a RESTful API backend.

### Key Features

- ✅ Create, read, update, and delete tasks
- ✅ Organize tasks by status (To Do, In Progress, Done)
- ✅ Set due dates and descriptions
- ✅ Real-time status updates
- ✅ Form validation with user-friendly error messages
- ✅ Responsive design with dark mode support
- ✅ RESTful API with proper error handling
- ✅ Type-safe frontend with TypeScript
- ✅ Comprehensive test coverage

### Tech Stack

**Frontend:**

- React 18 with TypeScript
- Vite (build tool and dev server)
- TailwindCSS (styling)
- Fetch API (HTTP client)

**Backend:**

- Spring Boot 3.5
- Java 21
- Spring Data JPA
- H2 Database (development)
- Gradle (build tool)

**Testing:**

- JUnit 5 & Mockito (backend unit tests)
- Hurl (manual API testing)

## Project Structure

```
task-manager/
├── frontend/           # React + TypeScript + Vite application
│   ├── src/
│   │   ├── components/   # React components
│   │   ├── services/     # API client
│   │   ├── types/        # TypeScript interfaces
│   │   └── ...
│   ├── package.json
│   └── README.md       # Frontend documentation
├── backend/            # Spring Boot + Java application
│   ├── src/
│   │   ├── main/java/com/taskmanager/
│   │   │   ├── controller/   # REST controllers
│   │   │   ├── service/      # Business logic
│   │   │   ├── repository/   # Data access
│   │   │   ├── model/        # Entities and enums
│   │   │   ├── exception/    # Error handling
│   │   │   └── config/       # Configuration
│   │   └── test/             # Unit tests
│   ├── hurl/                 # Manual API tests
│   ├── Dockerfile            # Production deployment
│   ├── build.gradle.kts
│   └── CLAUDE.md             # Backend documentation
├── docs/               # Specifications
│   └── SPEC.md        # API specification
├── devbox.json        # Development environment
└── README.md          # This file
```

## Configuration

### Environment Variables

**Frontend (`frontend/.env.production`):**

```bash
VITE_API_URL=http://localhost:8080/api  # Backend API URL
```

**Backend (`backend/src/main/resources/application.properties`):**

```properties
server.port=8080
spring.datasource.url=jdbc:h2:mem:taskdb
spring.h2.console.enabled=true
```

### Development Tools

This project uses [Devbox](https://www.jetify.com/devbox) to manage development dependencies (Java, Node.js, Hurl, Just). See [Deployment - Local Development](#local-deployment-development) for setup instructions.

**Task Runner:** This project uses [just](https://github.com/casey/just) as a command runner for common development tasks. Run `just` or `just --list` to see all available commands.

## Deployment

### Local Deployment (Development)

#### Prerequisites

- [Devbox](https://www.jetify.com/devbox) (recommended) or:
  - Java 21
  - Node.js 18+
  - npm

#### Setup with Devbox (Recommended)

1. **Install Devbox** (if not already installed):

   ```bash
   curl -fsSL https://get.jetify.com/devbox | bash
   ```

2. **Clone the repository** (if you haven't already):

   ```bash
   git clone <repository-url>
   cd task-manager
   ```

3. **Enter Devbox shell** (installs all dependencies automatically):

   ```bash
   devbox shell
   ```

   This sets up:
   - Java 21
   - Node.js and npm
   - Hurl (API testing tool)
   - Just (task runner)

#### Running the Backend

1. **Navigate to backend directory**:

   ```bash
   cd backend
   ```

2. **Run the application** (with Devbox):

   ```bash
   ./gradlew bootRun
   ```

   Or without Devbox (requires Java 21):

   ```bash
   ./gradlew bootRun
   ```

3. **Access the backend**:
   - API: http://localhost:8080/api
   - H2 Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:taskdb`
     - Username: `sa`
     - Password: (leave empty)

#### Running the Frontend

1. **Open a new terminal** and navigate to frontend:

   ```bash
   cd frontend
   ```

2. **Install dependencies** (first time only):

   ```bash
   npm install
   ```

3. **Start the development server**:

   ```bash
   npm run dev
   ```

4. **Access the application**:
   - Frontend: http://localhost:5173
   - The frontend automatically proxies `/api` requests to the backend

**Alternative with Just:**

You can use the `just` task runner for common operations:

- `just backend` - Start backend server
- `just frontend` - Start frontend dev server
- `just install` - Install all dependencies
- `just test-backend` - Run backend tests
- `just` - See all available commands

#### Testing the API with Hurl

The backend includes Hurl files for manual API testing:

```bash
cd backend/hurl

# Run a specific test
hurl 01-create-task.hurl

# Run all basic CRUD tests
hurl 0*.hurl

# Run validation tests
hurl validation/*.hurl

# Run error handling tests
hurl errors/*.hurl
```

See [`backend/hurl/README.md`](backend/hurl/README.md) for more details.

#### Running Backend Tests

```bash
cd backend
./gradlew test

# View test report
open build/reports/tests/test/index.html
```

### Production Deployment

This section covers deploying the frontend to Vercel and the backend to Fly.io.

---

#### Frontend Deployment on Vercel

[Vercel](https://vercel.com) is a cloud platform optimized for frontend frameworks like React and Vite.

##### Prerequisites

- GitHub account
- Vercel account (sign up at https://vercel.com)
- Code pushed to a GitHub repository

##### Option 1: Deploy via Vercel Web Interface

1. **Go to Vercel** (https://vercel.com) and sign in

2. **Import Project**:
   - Click "New Project"
   - Import your GitHub repository
   - Vercel will auto-detect it's a Vite project

3. **Configure Build Settings**:
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
   - **Install Command**: `npm install`

4. **Add Environment Variables**:
   - Key: `VITE_API_URL`
   - Value: `https://your-backend-app.fly.dev/api` (replace after deploying backend)

5. **Deploy**: Click "Deploy"

##### Option 2: Deploy via Vercel CLI

1. **Install Vercel CLI**:

   ```bash
   npm install -g vercel
   ```

2. **Navigate to frontend** and login:

   ```bash
   cd frontend
   vercel login
   ```

3. **Deploy**:

   ```bash
   vercel
   ```

4. **Set environment variable**:

   ```bash
   vercel env add VITE_API_URL
   # Enter: https://your-backend-app.fly.dev/api
   ```

5. **Redeploy** to apply environment variables:
   ```bash
   vercel --prod
   ```

##### Configure Frontend for Production API

Update `frontend/src/services/api.ts` to use environment variable:

```typescript
const API_BASE_URL = import.meta.env.VITE_API_URL || "/api";
```

Then create `frontend/.env.production`:

```bash
VITE_API_URL=https://your-backend-app.fly.dev/api
```

##### Update Backend CORS

After deployment, update `backend/src/main/java/com/taskmanager/config/WebConfig.java`:

```java
@Override
public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins(
                "http://localhost:5173",              // Development
                "https://your-app.vercel.app"         // Production
            )
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
}
```

---

#### Backend Deployment on Fly.io

[Fly.io](https://fly.io) is a platform for running full-stack apps globally using Docker containers.

##### Prerequisites

- Fly.io account (sign up at https://fly.io)
- Fly CLI installed

##### Install Fly CLI

**macOS/Linux**:

```bash
curl -L https://fly.io/install.sh | sh
```

**Windows** (PowerShell):

```powershell
iwr https://fly.io/install.ps1 -useb | iex
```

##### Deploy Steps

1. **Login to Fly.io**:

   ```bash
   fly auth login
   ```

2. **Navigate to backend**:

   ```bash
   cd backend
   ```

3. **Launch the app** (uses existing `Dockerfile`):

   ```bash
   fly launch
   ```

   This will:
   - Detect the Dockerfile
   - Prompt for app name (e.g., `taskmanager-api`)
   - Generate `fly.toml` configuration
   - Ask if you want to deploy now (say "no" first to review config)

4. **Review `fly.toml`**:

   Ensure these settings in the generated `fly.toml`:

   ```toml
   app = "taskmanager-api"  # Your app name
   primary_region = "fra"    # Or your preferred region

   [build]
     dockerfile = "Dockerfile"

   [http_service]
     internal_port = 8080
     force_https = true
     auto_stop_machines = true
     auto_start_machines = true

   [[services.http_checks]]
     interval = 10000
     grace_period = "5s"
     method = "get"
     path = "/api/tasks"
     protocol = "http"
     timeout = 2000
   ```

5. **Build the JAR locally** (optional, Docker will do this):

   ```bash
   ./gradlew bootJar
   ```

6. **Deploy the application**:

   ```bash
   fly deploy
   ```

7. **Check deployment status**:

   ```bash
   fly status
   ```

8. **View logs**:

   ```bash
   fly logs
   ```

9. **Open the application**:

   ```bash
   fly open
   ```

   Your API will be available at: `https://your-app-name.fly.dev/api/tasks`

##### Database Considerations

**Development (Current Setup):**

- Uses H2 in-memory database
- Data is lost when app restarts
- ⚠️ **Not recommended for production**

**Production (Recommended):**

Upgrade to PostgreSQL on Fly.io:

1. **Create Postgres cluster**:

   ```bash
   fly postgres create
   ```

2. **Attach to your app**:

   ```bash
   fly postgres attach <postgres-app-name>
   ```

3. **Update `application.properties`** for production:

   Create `src/main/resources/application-prod.properties`:

   ```properties
   spring.datasource.url=${DATABASE_URL}
   spring.datasource.driver-class-name=org.postgresql.Driver
   spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
   spring.jpa.hibernate.ddl-auto=update
   ```

4. **Add PostgreSQL dependency** to `build.gradle.kts`:

   ```kotlin
   runtimeOnly("org.postgresql:postgresql")
   ```

5. **Set active profile** in `fly.toml`:
   ```toml
   [env]
     SPRING_PROFILES_ACTIVE = "prod"
   ```

##### Environment Variables / Secrets

Set environment variables using Fly secrets:

```bash
# Set a secret
fly secrets set SPRING_PROFILES_ACTIVE=prod

# List secrets
fly secrets list

# Remove a secret
fly secrets unset SECRET_NAME
```

##### Scaling

```bash
# Scale to 2 machines
fly scale count 2

# Set VM size
fly scale vm shared-cpu-1x

# Auto-scale based on traffic
fly autoscale set min=1 max=3
```

##### CI/CD with GitHub Actions

Create `.github/workflows/deploy-backend.yml`:

```yaml
name: Deploy Backend to Fly.io

on:
  push:
    branches: [main]
    paths:
      - "backend/**"

jobs:
  deploy:
    name: Deploy
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: superfly/flyctl-actions/setup-flyctl@master
      - name: Deploy to Fly.io
        run: flyctl deploy --remote-only
        working-directory: ./backend
        env:
          FLY_API_TOKEN: ${{ secrets.FLY_API_TOKEN }}
```

---

### Production Readiness Checklist

Before going to production, ensure you've completed:

#### Backend

- [ ] Switch from H2 to PostgreSQL database
- [ ] Set proper CORS origins (not `*`)
- [ ] Configure environment-specific properties
- [ ] Enable HTTPS (handled by Fly.io)
- [ ] Set up monitoring and logging
- [ ] Configure proper error handling
- [ ] Review security headers
- [ ] Set up database backups
- [ ] Test API endpoints in production
- [ ] Configure health checks

#### Frontend

- [ ] Set `VITE_API_URL` to production backend
- [ ] Test all features with production API
- [ ] Verify CORS is working
- [ ] Check responsive design on mobile
- [ ] Test dark/light mode
- [ ] Optimize bundle size
- [ ] Configure custom domain (optional)
- [ ] Set up error tracking (e.g., Sentry)

#### DevOps

- [ ] Set up CI/CD pipeline
- [ ] Configure automated tests in CI
- [ ] Set up staging environment
- [ ] Document deployment process
- [ ] Configure monitoring/alerting
- [ ] Set up log aggregation

## API Documentation

The REST API follows standard conventions:

- **Base URL**: `http://localhost:8080/api` (development) or `https://your-app.fly.dev/api` (production)
- **Endpoints**: `/tasks`
- **Methods**: GET, POST, PUT, DELETE
- **Response Format**: JSON

See [`docs/SPEC.md`](docs/SPEC.md) for the complete API specification.

## Testing

### Backend Unit Tests

```bash
cd backend
./gradlew test
```

Test coverage:

- Service layer: 100% (12 tests)
- Controller layer: 95%+ (17 tests)
- Repository layer: 80%+ (10 tests)

### Manual API Testing with Hurl

```bash
cd backend/hurl
hurl 0*.hurl validation/*.hurl errors/*.hurl
```

See [`backend/hurl/README.md`](backend/hurl/README.md) for details.

## Development

### Project Documentation

- **Frontend**: See [`frontend/README.md`](frontend/README.md)
- **Backend**: See [`backend/CLAUDE.md`](backend/CLAUDE.md)
- **API Spec**: See [`docs/SPEC.md`](docs/SPEC.md)
- **Hurl Tests**: See [`backend/hurl/README.md`](backend/hurl/README.md)

### Quick Commands with Just

This project uses `just` as a task runner for common development tasks. All commands should be run from the project root inside a Devbox shell.

**Common Commands:**

```bash
# List all available commands
just

# Install all dependencies
just install

# Start backend server
just backend

# Start frontend dev server
just frontend

# Run backend tests
just test-backend

# Run backend tests and open report
just test-backend-report

# Run Hurl API tests (requires backend running)
just test-api

# Build backend JAR
just build-backend

# Build frontend for production
just build-frontend

# Build everything
just build

# Clean all build artifacts
just clean

# Build Docker image
just docker-build

# Run Docker container
just docker-run

# Deploy backend to Fly.io
just deploy-backend

# Deploy frontend to Vercel
just deploy-frontend

# Show H2 database console info
just db-console

# Show project status
just status
```

See the `justfile` in the project root for all available commands and their implementations.

### Code Structure

- Follow existing patterns for consistency
- Use TypeScript for type safety
- Write tests for new features
- Use meaningful commit messages
- Update documentation as needed

## Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is for educational purposes.

## Support

For issues or questions:

- Check the documentation in `/frontend`, `/backend`, and `/docs`
- Review the [API Specification](docs/SPEC.md)
- Look at existing [Hurl tests](backend/hurl/) for API examples

---

Built with ❤️ using React, Spring Boot, and modern web technologies.
