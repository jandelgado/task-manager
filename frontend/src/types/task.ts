export enum TaskStatus {
  TODO = 'TODO',
  IN_PROGRESS = 'IN_PROGRESS',
  DONE = 'DONE'
}

export interface Task {
  id?: number;
  title: string;
  description?: string;
  status: TaskStatus;
  dueDate?: string; // ISO date string (YYYY-MM-DD)
}

export interface ValidationError {
  errors: {
    [field: string]: string;
  };
}

export interface NotFoundError {
  error: string;
}
