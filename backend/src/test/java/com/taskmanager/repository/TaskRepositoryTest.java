package com.taskmanager.repository;

import com.taskmanager.model.Task;
import com.taskmanager.model.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TestEntityManager entityManager;

    // Helper method for creating test data
    private Task createValidTask() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setStatus(TaskStatus.TODO);
        task.setDueDate(LocalDate.of(2026, 1, 15));
        return task;
    }

    // CRUD Operations tests

    @Test
    void save_withValidTask_persistsTaskAndGeneratesId() {
        // Arrange
        Task task = createValidTask();

        // Act
        Task savedTask = taskRepository.save(task);

        // Assert
        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("Test Task");
        assertThat(savedTask.getDescription()).isEqualTo("Test Description");
        assertThat(savedTask.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(savedTask.getDueDate()).isEqualTo(LocalDate.of(2026, 1, 15));
    }

    @Test
    void findById_whenExists_returnsTask() {
        // Arrange
        Task task = createValidTask();
        Task savedTask = entityManager.persistAndFlush(task);

        // Act
        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());

        // Assert
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getId()).isEqualTo(savedTask.getId());
        assertThat(foundTask.get().getTitle()).isEqualTo("Test Task");
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        // Act
        Optional<Task> foundTask = taskRepository.findById(999L);

        // Assert
        assertThat(foundTask).isEmpty();
    }

    @Test
    void findAll_returnsAllTasks() {
        // Arrange
        Task task1 = createValidTask();
        Task task2 = createValidTask();
        task2.setTitle("Task 2");

        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.flush();

        // Act
        List<Task> tasks = taskRepository.findAll();

        // Assert
        assertThat(tasks)
                .isNotNull()
                .hasSize(2)
                .extracting(Task::getTitle)
                .containsExactlyInAnyOrder("Test Task", "Task 2");
    }

    @Test
    void delete_removesTaskFromDatabase() {
        // Arrange
        Task task = createValidTask();
        Task savedTask = entityManager.persistAndFlush(task);
        Long taskId = savedTask.getId();

        // Act
        taskRepository.delete(savedTask);
        entityManager.flush();

        // Assert
        Optional<Task> deletedTask = taskRepository.findById(taskId);
        assertThat(deletedTask).isEmpty();
    }

    // Validation tests

    @Test
    void save_withNullTitle_throwsException() {
        // Arrange
        Task task = createValidTask();
        task.setTitle(null);

        // Act & Assert
        try {
            taskRepository.save(task);
            entityManager.flush();
            // If we reach here, the test should fail
            throw new AssertionError("Expected exception was not thrown");
        } catch (Exception e) {
            // Exception is expected (constraint violation)
            assertThat(e).isNotNull();
        }
    }

    @Test
    void save_withNullStatus_throwsException() {
        // Arrange
        Task task = createValidTask();
        task.setStatus(null);

        // Act & Assert
        try {
            taskRepository.save(task);
            entityManager.flush();
            // If we reach here, the test should fail
            throw new AssertionError("Expected exception was not thrown");
        } catch (Exception e) {
            // Exception is expected (constraint violation)
            assertThat(e).isNotNull();
        }
    }

    // Data Integrity tests

    @Test
    void save_withDefaultStatus_persistsAsTODO() {
        // Arrange
        Task task = new Task();
        task.setTitle("Task with default status");
        task.setStatus(TaskStatus.TODO);

        // Act
        Task savedTask = taskRepository.save(task);
        entityManager.flush();

        // Assert
        assertThat(savedTask.getStatus()).isEqualTo(TaskStatus.TODO);
    }

    @Test
    void save_withAllFields_persistsAllFieldsCorrectly() {
        // Arrange
        Task task = new Task();
        task.setTitle("Complete Task");
        task.setDescription("Full description");
        task.setStatus(TaskStatus.IN_PROGRESS);
        task.setDueDate(LocalDate.of(2026, 12, 31));

        // Act
        Task savedTask = taskRepository.save(task);
        entityManager.flush();
        entityManager.clear(); // Clear the persistence context

        // Re-fetch from database to verify
        Optional<Task> refetchedTask = taskRepository.findById(savedTask.getId());

        // Assert
        assertThat(refetchedTask).isPresent();
        Task task1 = refetchedTask.get();
        assertThat(task1.getTitle()).isEqualTo("Complete Task");
        assertThat(task1.getDescription()).isEqualTo("Full description");
        assertThat(task1.getStatus()).isEqualTo(TaskStatus.IN_PROGRESS);
        assertThat(task1.getDueDate()).isEqualTo(LocalDate.of(2026, 12, 31));
    }

    @Test
    void save_andUpdate_modifiesExistingTask() {
        // Arrange
        Task task = createValidTask();
        Task savedTask = taskRepository.save(task);
        entityManager.flush();

        // Act - Update the task
        savedTask.setTitle("Updated Title");
        savedTask.setStatus(TaskStatus.DONE);
        Task updatedTask = taskRepository.save(savedTask);
        entityManager.flush();
        entityManager.clear();

        // Assert
        Optional<Task> refetchedTask = taskRepository.findById(updatedTask.getId());
        assertThat(refetchedTask).isPresent();
        assertThat(refetchedTask.get().getTitle()).isEqualTo("Updated Title");
        assertThat(refetchedTask.get().getStatus()).isEqualTo(TaskStatus.DONE);
    }

}
