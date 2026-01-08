import { Task, TaskStatus } from '../types/task';
import { TaskCard } from './TaskCard';

interface TaskListProps {
  tasks: Task[];
  onEdit: (task: Task) => void;
  onDelete: (id: number) => void;
  onStatusChange: (task: Task, status: TaskStatus) => void;
}

export function TaskList({ tasks, onEdit, onDelete, onStatusChange }: TaskListProps) {
  const groupedTasks = {
    [TaskStatus.TODO]: tasks.filter(t => t.status === TaskStatus.TODO),
    [TaskStatus.IN_PROGRESS]: tasks.filter(t => t.status === TaskStatus.IN_PROGRESS),
    [TaskStatus.DONE]: tasks.filter(t => t.status === TaskStatus.DONE),
  };

  const statusLabels = {
    [TaskStatus.TODO]: 'To Do',
    [TaskStatus.IN_PROGRESS]: 'In Progress',
    [TaskStatus.DONE]: 'Done',
  };

  const statusColors = {
    [TaskStatus.TODO]: 'bg-gray-100 dark:bg-gray-800',
    [TaskStatus.IN_PROGRESS]: 'bg-blue-100 dark:bg-blue-900',
    [TaskStatus.DONE]: 'bg-green-100 dark:bg-green-900',
  };

  if (tasks.length === 0) {
    return (
      <div className="text-center py-12 bg-white dark:bg-gray-800 rounded-lg shadow">
        <p className="text-gray-500 dark:text-gray-400">No tasks yet. Create one to get started!</p>
      </div>
    );
  }

  return (
    <div className="space-y-6">
      {Object.entries(groupedTasks).map(([status, statusTasks]) => (
        <div key={status}>
          <div className="flex items-center mb-3">
            <span className={`px-3 py-1 rounded-full text-sm font-medium ${statusColors[status as TaskStatus]}`}>
              {statusLabels[status as TaskStatus]} ({statusTasks.length})
            </span>
          </div>
          <div className="space-y-3">
            {statusTasks.length === 0 ? (
              <div className="bg-white dark:bg-gray-800 rounded-lg shadow p-4 text-center text-gray-400">
                No tasks in this status
              </div>
            ) : (
              statusTasks.map(task => (
                <TaskCard
                  key={task.id}
                  task={task}
                  onEdit={onEdit}
                  onDelete={onDelete}
                  onStatusChange={onStatusChange}
                />
              ))
            )}
          </div>
        </div>
      ))}
    </div>
  );
}
