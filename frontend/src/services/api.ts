import { Task } from '../types/task';

// Use environment variable for API URL, fallback to /api for local development with Vite proxy
const API_BASE_URL = import.meta.env.VITE_API_URL || '/api';

class ApiError extends Error {
  constructor(
    message: string,
    public status: number,
    public data?: unknown
  ) {
    super(message);
    this.name = 'ApiError';
  }
}

async function handleResponse<T>(response: Response): Promise<T> {
  if (!response.ok) {
    const data = await response.json().catch(() => ({}));
    throw new ApiError(
      `HTTP ${response.status}: ${response.statusText}`,
      response.status,
      data
    );
  }

  // Handle 204 No Content
  if (response.status === 204) {
    return undefined as T;
  }

  return response.json();
}

export const taskApi = {
  // Get all tasks
  getAllTasks: async (): Promise<Task[]> => {
    const response = await fetch(`${API_BASE_URL}/tasks`);
    return handleResponse<Task[]>(response);
  },

  // Get task by ID
  getTaskById: async (id: number): Promise<Task> => {
    const response = await fetch(`${API_BASE_URL}/tasks/${id}`);
    return handleResponse<Task>(response);
  },

  // Create a new task
  createTask: async (task: Omit<Task, 'id'>): Promise<Task> => {
    const response = await fetch(`${API_BASE_URL}/tasks`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(task),
    });
    return handleResponse<Task>(response);
  },

  // Update an existing task
  updateTask: async (id: number, task: Omit<Task, 'id'>): Promise<Task> => {
    const response = await fetch(`${API_BASE_URL}/tasks/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(task),
    });
    return handleResponse<Task>(response);
  },

  // Delete a task
  deleteTask: async (id: number): Promise<void> => {
    const response = await fetch(`${API_BASE_URL}/tasks/${id}`, {
      method: 'DELETE',
    });
    return handleResponse<void>(response);
  },
};

export { ApiError };
