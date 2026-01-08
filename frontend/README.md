# Task Manager Frontend

React + TypeScript + Vite + TailwindCSS application for managing tasks.

## Features

- ✅ Create, read, update, and delete tasks
- ✅ Organize tasks by status (To Do, In Progress, Done)
- ✅ Set due dates for tasks
- ✅ Real-time status updates
- ✅ Form validation with error messages
- ✅ Responsive design with dark mode support
- ✅ Type-safe API client

## Tech Stack

- **React 18** - UI library
- **TypeScript** - Type safety
- **Vite** - Build tool and dev server
- **TailwindCSS** - Utility-first CSS framework
- **Fetch API** - HTTP client

## Project Structure

```
src/
├── components/       # Reusable UI components
│   ├── TaskCard.tsx
│   ├── TaskForm.tsx
│   └── TaskList.tsx
├── pages/           # Page-level components (future use)
├── services/        # API client
│   └── api.ts
├── types/           # TypeScript interfaces
│   └── task.ts
├── hooks/           # Custom React hooks (future use)
├── utils/           # Helper functions (future use)
├── App.tsx          # Main application component
├── main.tsx         # Application entry point
└── index.css        # Global styles with Tailwind
```

## Getting Started

### Prerequisites

- Node.js 18+ and npm
- Backend server running on http://localhost:8080

### Installation

```bash
cd frontend
npm install
```

### Development

Start the development server:

```bash
npm run dev
```

The app will be available at http://localhost:5173

The dev server is configured to proxy `/api` requests to the backend at http://localhost:8080.

### Build

Create a production build:

```bash
npm run build
```

The built files will be in the `dist/` directory.

### Preview Production Build

```bash
npm run preview
```

### Linting

```bash
npm run lint
```

## API Integration

The frontend communicates with the backend REST API through the `taskApi` service in `src/services/api.ts`.

### Available API Methods

- `getAllTasks()` - Fetch all tasks
- `getTaskById(id)` - Fetch a single task
- `createTask(task)` - Create a new task
- `updateTask(id, task)` - Update an existing task
- `deleteTask(id)` - Delete a task

### Error Handling

The API service includes error handling for:
- Validation errors (400) - Displayed in form fields
- Not found errors (404) - Displayed as error messages
- Network errors - Displayed as error messages

## Components

### App.tsx
Main application component that manages state and orchestrates the task workflow.

### TaskForm.tsx
Form component for creating and editing tasks with validation.

### TaskList.tsx
Groups and displays tasks by status (To Do, In Progress, Done).

### TaskCard.tsx
Individual task card with inline status updates and action buttons.

## Type Safety

All API interactions are type-safe using TypeScript interfaces:

```typescript
interface Task {
  id?: number;
  title: string;
  description?: string;
  status: TaskStatus;
  dueDate?: string;
}

enum TaskStatus {
  TODO = 'TODO',
  IN_PROGRESS = 'IN_PROGRESS',
  DONE = 'DONE'
}
```

## Styling

The application uses TailwindCSS for styling with:
- Responsive design (mobile-first)
- Dark mode support (based on system preferences)
- Utility classes for rapid development
- Consistent spacing and colors

## Future Enhancements

- [ ] Add task filtering and sorting
- [ ] Implement task search
- [ ] Add task categories/tags
- [ ] Implement drag-and-drop for status changes
- [ ] Add task priority levels
- [ ] Implement user authentication
- [ ] Add task comments
- [ ] Export tasks to various formats
- [ ] Add keyboard shortcuts
- [ ] Implement offline support with service workers

## Contributing

When adding new features:
1. Create components in the appropriate directory
2. Add TypeScript types for new data structures
3. Update the API service for new endpoints
4. Follow existing naming conventions
5. Test with the running backend

## Troubleshooting

### Backend Connection Issues

If you see "Failed to load tasks" errors:
1. Ensure the backend is running on http://localhost:8080
2. Check that the Vite proxy is configured correctly in `vite.config.ts`
3. Verify CORS is enabled on the backend

### Build Errors

If you encounter TypeScript errors:
1. Run `npm install` to ensure all dependencies are installed
2. Check `tsconfig.json` for proper configuration
3. Ensure you're using Node.js 18+

## Learn More

- [React Documentation](https://react.dev)
- [TypeScript Documentation](https://www.typescriptlang.org/docs/)
- [Vite Documentation](https://vitejs.dev)
- [TailwindCSS Documentation](https://tailwindcss.com/docs)
