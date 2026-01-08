package com.taskmanager.service;

import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    // Helper methods for creating test data
    private Task createValidTask() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.TODO);
        task.setDueDate(LocalDate.of(2026, 1, 15));
        return task;
    }

    private Task createTaskWithoutId() {
        Task task = createValidTask();
        task.setId(null);
        return task;
    }

    // getAllTasks() tests

    @Test
    void getAllTasks_whenTasksExist_returnsTaskList() {
        // Arrange
        Task task1 = createValidTask();
        Task task2 = createValidTask();
        task2.setId(2L);
        task2.setTitle("Task 2");
        List<Task> expectedTasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(expectedTasks);

        // Act
        List<Task> result = taskService.getAllTasks();

        // Assert
        assertThat(result)
                .isNotNull()
                .hasSize(2)
                .containsExactly(task1, task2);
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void getAllTasks_whenNoTasks_returnsEmptyList() {
        // Arrange
        when(taskRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Task> result = taskService.getAllTasks();

        // Assert
        assertThat(result)
                .isNotNull()
                .isEmpty();
        verify(taskRepository, times(1)).findAll();
    }

    // getTaskById() tests

    @Test
    void getTaskById_whenTaskExists_returnsTask() {
        // Arrange
        Task expectedTask = createValidTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(expectedTask));

        // Act
        Task result = taskService.getTaskById(1L);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(expectedTask);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Task");
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getTaskById_whenTaskNotFound_throwsTaskNotFoundException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> taskService.getTaskById(999L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found");
        verify(taskRepository, times(1)).findById(999L);
    }

    // createTask() tests

    @Test
    void createTask_withValidTask_setsIdToNullAndSaves() {
        // Arrange
        Task taskWithId = createValidTask();
        Task taskWithoutId = createTaskWithoutId();
        Task savedTask = createValidTask();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        taskService.createTask(taskWithId);

        // Assert
        verify(taskRepository, times(1)).save(argThat(task ->
                task.getId() == null &&
                task.getTitle().equals("Test Task")
        ));
    }

    @Test
    void createTask_withValidTask_returnsCreatedTask() {
        // Arrange
        Task inputTask = createTaskWithoutId();
        Task savedTask = createValidTask();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        Task result = taskService.createTask(inputTask);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(savedTask);
        assertThat(result.getId()).isEqualTo(1L);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    // updateTask() tests

    @Test
    void updateTask_whenTaskExists_updatesAllFields() {
        // Arrange
        Task existingTask = createValidTask();
        Task updateData = new Task();
        updateData.setTitle("Updated Title");
        updateData.setDescription("Updated Description");
        updateData.setStatus(TaskStatus.IN_PROGRESS);
        updateData.setDueDate(LocalDate.of(2026, 2, 20));

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        // Act
        taskService.updateTask(1L, updateData);

        // Assert
        verify(taskRepository, times(1)).save(argThat(task ->
                task.getTitle().equals("Updated Title") &&
                task.getDescription().equals("Updated Description") &&
                task.getStatus() == TaskStatus.IN_PROGRESS &&
                task.getDueDate().equals(LocalDate.of(2026, 2, 20))
        ));
    }

    @Test
    void updateTask_whenTaskExists_returnsSavedTask() {
        // Arrange
        Task existingTask = createValidTask();
        Task updateData = createValidTask();
        updateData.setTitle("Updated");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        // Act
        Task result = taskService.updateTask(1L, updateData);

        // Assert
        assertThat(result)
                .isNotNull()
                .isEqualTo(existingTask);
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }

    @Test
    void updateTask_whenTaskNotFound_throwsTaskNotFoundException() {
        // Arrange
        Task updateData = createValidTask();
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> taskService.updateTask(999L, updateData))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found");
        verify(taskRepository, times(1)).findById(999L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void updateTask_withNullDescription_updatesCorrectly() {
        // Arrange
        Task existingTask = createValidTask();
        Task updateData = new Task();
        updateData.setTitle("Updated Title");
        updateData.setDescription(null);
        updateData.setStatus(TaskStatus.TODO);
        updateData.setDueDate(LocalDate.of(2026, 1, 15));

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

        // Act
        taskService.updateTask(1L, updateData);

        // Assert
        verify(taskRepository, times(1)).save(argThat(task ->
                task.getDescription() == null &&
                task.getTitle().equals("Updated Title")
        ));
    }

    // deleteTask() tests

    @Test
    void deleteTask_whenTaskExists_deletesTask() {
        // Arrange
        Task existingTask = createValidTask();
        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        // Act
        taskService.deleteTask(1L);

        // Assert
        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(existingTask);
    }

    @Test
    void deleteTask_whenTaskNotFound_throwsTaskNotFoundException() {
        // Arrange
        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> taskService.deleteTask(999L))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessage("Task not found");
        verify(taskRepository, times(1)).findById(999L);
        verify(taskRepository, never()).delete(any(Task.class));
    }

}
