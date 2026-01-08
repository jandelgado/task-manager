import { useState, useEffect } from 'react';
import { taskApi, ApiError } from './services/api';
import { Task, TaskStatus } from './types/task';
import { TaskList } from './components/TaskList';
import { TaskForm } from './components/TaskForm';

export function App() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [editingTask, setEditingTask] = useState<Task | null>(null);

  // Load tasks on mount
  useEffect(() => {
    loadTasks();
  }, []);

  const loadTasks = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await taskApi.getAllTasks();
      setTasks(data);
    } catch (err) {
      if (err instanceof ApiError) {
        setError(`Failed to load tasks: ${err.message}`);
      } else {
        setError('Failed to load tasks');
      }
    } finally {
      setLoading(false);
    }
  };

  const handleCreateTask = async (task: Omit<Task, 'id'>) => {
    try {
      setError(null);
      const newTask = await taskApi.createTask(task);
      setTasks([...tasks, newTask]);
    } catch (err) {
      if (err instanceof ApiError) {
        throw err; // Let the form handle validation errors
      }
      setError('Failed to create task');
      throw err;
    }
  };

  const handleUpdateTask = async (id: number, task: Omit<Task, 'id'>) => {
    try {
      setError(null);
      const updatedTask = await taskApi.updateTask(id, task);
      setTasks(tasks.map(t => t.id === id ? updatedTask : t));
      setEditingTask(null);
    } catch (err) {
      if (err instanceof ApiError) {
        throw err; // Let the form handle validation errors
      }
      setError('Failed to update task');
      throw err;
    }
  };

  const handleDeleteTask = async (id: number) => {
    if (!confirm('Are you sure you want to delete this task?')) {
      return;
    }

    try {
      setError(null);
      await taskApi.deleteTask(id);
      setTasks(tasks.filter(t => t.id !== id));
    } catch (err) {
      if (err instanceof ApiError) {
        setError(`Failed to delete task: ${err.message}`);
      } else {
        setError('Failed to delete task');
      }
    }
  };

  const handleStatusChange = async (task: Task, newStatus: TaskStatus) => {
    if (!task.id) return;

    try {
      setError(null);
      const updatedTask = await taskApi.updateTask(task.id, {
        ...task,
        status: newStatus,
      });
      setTasks(tasks.map(t => t.id === task.id ? updatedTask : t));
    } catch (err) {
      if (err instanceof ApiError) {
        setError(`Failed to update task status: ${err.message}`);
      } else {
        setError('Failed to update task status');
      }
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 dark:bg-gray-900">
      <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">
          <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-8">
            Task Manager
          </h1>

          {error && (
            <div className="mb-4 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded">
              {error}
            </div>
          )}

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
            <div className="lg:col-span-2">
              {loading ? (
                <div className="text-center py-12">
                  <div className="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900 dark:border-white"></div>
                  <p className="mt-2 text-gray-600 dark:text-gray-400">Loading tasks...</p>
                </div>
              ) : (
                <TaskList
                  tasks={tasks}
                  onEdit={setEditingTask}
                  onDelete={handleDeleteTask}
                  onStatusChange={handleStatusChange}
                />
              )}
            </div>

            <div className="lg:col-span-1">
              <TaskForm
                task={editingTask}
                onSubmit={editingTask && editingTask.id
                  ? (task) => handleUpdateTask(editingTask.id!, task)
                  : handleCreateTask
                }
                onCancel={() => setEditingTask(null)}
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
