package com.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taskmanager.exception.TaskNotFoundException;
import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import com.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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

    // GET /api/tasks tests

    @Test
    void getAllTasks_returnsOkWithTaskList() throws Exception {
        // Arrange
        Task task1 = createValidTask();
        Task task2 = createValidTask();
        task2.setId(2L);
        task2.setTitle("Task 2");
        List<Task> tasks = Arrays.asList(task1, task2);

        when(taskService.getAllTasks()).thenReturn(tasks);

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Test Task"))
                .andExpect(jsonPath("$[0].status").value("TODO"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Task 2"));

        verify(taskService, times(1)).getAllTasks();
    }

    @Test
    void getAllTasks_whenEmpty_returnsEmptyArray() throws Exception {
        // Arrange
        when(taskService.getAllTasks()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(taskService, times(1)).getAllTasks();
    }

    // GET /api/tasks/{id} tests

    @Test
    void getTaskById_whenExists_returnsOkWithTask() throws Exception {
        // Arrange
        Task task = createValidTask();
        when(taskService.getTaskById(1L)).thenReturn(task);

        // Act & Assert
        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.dueDate").value("2026-01-15"));

        verify(taskService, times(1)).getTaskById(1L);
    }

    @Test
    void getTaskById_whenNotFound_returns404WithErrorMessage() throws Exception {
        // Arrange
        when(taskService.getTaskById(999L)).thenThrow(new TaskNotFoundException(999L));

        // Act & Assert
        mockMvc.perform(get("/api/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.error").value("Task not found"));

        verify(taskService, times(1)).getTaskById(999L);
    }

    // POST /api/tasks tests

    @Test
    void createTask_withValidTask_returns201WithCreatedTask() throws Exception {
        // Arrange
        Task inputTask = createTaskWithoutId();
        Task createdTask = createValidTask();

        when(taskService.createTask(any(Task.class))).thenReturn(createdTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputTask)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.status").value("TODO"));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    void createTask_withBlankTitle_returns400WithValidationError() throws Exception {
        // Arrange
        Task task = createTaskWithoutId();
        task.setTitle("");

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors.title").exists());

        verify(taskService, never()).createTask(any(Task.class));
    }

    @Test
    void createTask_withTitleTooLong_returns400WithValidationError() throws Exception {
        // Arrange
        Task task = createTaskWithoutId();
        task.setTitle("a".repeat(101)); // 101 characters

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors.title").exists());

        verify(taskService, never()).createTask(any(Task.class));
    }

    @Test
    void createTask_withNullStatus_returns400WithValidationError() throws Exception {
        // Arrange
        Task task = createTaskWithoutId();
        task.setStatus(null);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errors.status").exists());

        verify(taskService, never()).createTask(any(Task.class));
    }

    @Test
    void createTask_withMinimalFields_returns201() throws Exception {
        // Arrange
        Task minimalTask = new Task();
        minimalTask.setTitle("Minimal Task");
        minimalTask.setStatus(TaskStatus.TODO);

        Task createdTask = new Task();
        createdTask.setId(1L);
        createdTask.setTitle("Minimal Task");
        createdTask.setStatus(TaskStatus.TODO);

        when(taskService.createTask(any(Task.class))).thenReturn(createdTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(minimalTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Minimal Task"))
                .andExpect(jsonPath("$.status").value("TODO"));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    // PUT /api/tasks/{id} tests

    @Test
    void updateTask_withValidTask_returns200WithUpdatedTask() throws Exception {
        // Arrange
        Task updateTask = createValidTask();
        updateTask.setTitle("Updated Task");

        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(updateTask);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTask)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Task"));

        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

    @Test
    void updateTask_whenNotFound_returns404() throws Exception {
        // Arrange
        Task updateTask = createValidTask();
        when(taskService.updateTask(eq(999L), any(Task.class)))
                .thenThrow(new TaskNotFoundException(999L));

        // Act & Assert
        mockMvc.perform(put("/api/tasks/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateTask)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Task not found"));

        verify(taskService, times(1)).updateTask(eq(999L), any(Task.class));
    }

    @Test
    void updateTask_withInvalidTitle_returns400() throws Exception {
        // Arrange
        Task task = createValidTask();
        task.setTitle(""); // Blank title

        // Act & Assert
        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.title").exists());

        verify(taskService, never()).updateTask(any(), any());
    }

    @Test
    void updateTask_withDescriptionTooLong_returns400() throws Exception {
        // Arrange
        Task task = createValidTask();
        task.setDescription("a".repeat(501)); // 501 characters

        // Act & Assert
        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.description").exists());

        verify(taskService, never()).updateTask(any(), any());
    }

    // DELETE /api/tasks/{id} tests

    @Test
    void deleteTask_whenExists_returns204NoContent() throws Exception {
        // Arrange
        doNothing().when(taskService).deleteTask(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent());

        verify(taskService, times(1)).deleteTask(1L);
    }

    @Test
    void deleteTask_whenNotFound_returns404() throws Exception {
        // Arrange
        doThrow(new TaskNotFoundException(999L)).when(taskService).deleteTask(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/tasks/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Task not found"));

        verify(taskService, times(1)).deleteTask(999L);
    }

    // Edge case tests

    @Test
    void createTask_withAllFieldsPopulated_returns201() throws Exception {
        // Arrange
        Task fullTask = createTaskWithoutId();
        Task createdTask = createValidTask();

        when(taskService.createTask(any(Task.class))).thenReturn(createdTask);

        // Act & Assert
        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fullTask)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.dueDate").value("2026-01-15"));

        verify(taskService, times(1)).createTask(any(Task.class));
    }

    @Test
    void updateTask_withNullableFieldsSetToNull_returns200() throws Exception {
        // Arrange
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Task with nulls");
        task.setDescription(null);
        task.setStatus(TaskStatus.TODO);
        task.setDueDate(null);

        when(taskService.updateTask(eq(1L), any(Task.class))).thenReturn(task);

        // Act & Assert
        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(task)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Task with nulls"))
                .andExpect(jsonPath("$.status").value("TODO"));

        verify(taskService, times(1)).updateTask(eq(1L), any(Task.class));
    }

}
