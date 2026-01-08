# Taskmanager Frontend - React Application

## Tech Stack

- React 18+ with TypeScript
- Vite
- TailwindCSS
- Fetch API for HTTP requests

## Directory Structure

```
src/
├── components/     # Reusable UI components
├── pages/          # Page-level components
├── services/       # API client
├── types/          # TypeScript interfaces
├── hooks/          # Custom React hooks
└── utils/          # Helper functions
```

## Conventions

- Functional components with hooks
- Named exports for components
- API service centralizes all backend calls
- Form validation before submission
- Display user-friendly error messages on API failures

## Commands

- `npm run dev` - Start dev server
- `npm run build` - Production build
- `npm run lint` - Run ESLint
