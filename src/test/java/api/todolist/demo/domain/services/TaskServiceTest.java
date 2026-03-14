package api.todolist.demo.domain.services;

import api.todolist.demo.domain.model.AppUser;
import api.todolist.demo.domain.model.Task;
import api.todolist.demo.domain.providers.IdentityProvider;
import api.todolist.demo.domain.repository.AppUserRepository;
import api.todolist.demo.domain.repository.TaskRepository;
import api.todolist.demo.persistence.adapter.AppUserAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private IdentityProvider identityProvider;

    @InjectMocks
    private TaskService taskService;

    @Test
    void givingValidTaskId_whenGetTask_thenNoExceptionsAndReturnTask() {
        // given
        when(identityProvider.currentIdentity()).thenReturn(buildValidUser());

        Long taskId = 10L;
        Task taskFromDb = new Task();
        taskFromDb.setId(taskId);

        when(taskRepository.getTaskByIdAndUserId(taskId, 99L)).thenReturn(taskFromDb);

        // when
        Task result = assertDoesNotThrow(() -> taskService.getTaskById(taskId));

        // then
        assertSame(taskFromDb, result);
    }

    @Test
    void givingValidTaskAndId_whenUpdateTask_thenNoExceptionsAndSavedToDb() {
        Long userId = buildValidUser().getId();
        Long taskId = 1L;
        LocalDateTime now = LocalDateTime.now();

        Task oldTask = new Task();
        oldTask.setId(taskId);
        oldTask.setAppUserId(userId);
        oldTask.setTitle("old task");
        oldTask.setIsCompleted(true);
        oldTask.setCreatedAt(now);
        oldTask.setUpdatedAt(now);

        Task updatedTask = new Task();
        String newTitle = "new task";
        updatedTask.setTitle(newTitle);

        when(identityProvider.currentIdentity()).thenReturn(buildValidUser());
        when(taskRepository.getTaskByIdAndUserId(taskId, userId)).thenReturn(oldTask);

        assertDoesNotThrow(() -> taskService.updateTaskById(taskId, updatedTask));

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(1)).save(captor.capture());


        Task saved = captor.getValue();

        assertEquals(userId, saved.getAppUserId());
        assertEquals(taskId, saved.getId());
        assertEquals(newTitle, saved.getTitle());
        assertEquals(true, saved.getIsCompleted());
        assertNotNull(saved.getUpdatedAt());
    }


    @Test
    void givingValidTask_whenCreateTask_thenNoExceptionsAndSavedToDb() {
        // given
        Task task = buildValidTask();

        when(identityProvider.currentIdentity()).thenReturn(buildValidUser());

        // when
        assertDoesNotThrow(() -> taskService.createTask(task));

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(1)).save(captor.capture());

        Task saved = captor.getValue();

        // the service overwrites these fields:
        assertEquals(99L, saved.getAppUserId());
        assertEquals(false, saved.getIsCompleted());

        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
        assertEquals(saved.getCreatedAt(), saved.getUpdatedAt()); // set to same "now"
    }

    @Test
    void givingInvalidTask_whenCreateTask_thenExceptionShouldBeThrown() {
        // case 1: null task
        IllegalArgumentException ex1 =
                assertThrows(IllegalArgumentException.class, () -> taskService.createTask(null));
        assertEquals("Task is required", ex1.getMessage());
        verify(taskRepository, never()).save(any());

        // reset interactions for next case
        reset(taskRepository, identityProvider);

        // case 2: blank title
        Task invalid = buildInvalidTask();
        IllegalArgumentException ex2 =
                assertThrows(IllegalArgumentException.class, () -> taskService.createTask(invalid));
        assertEquals("Task title is required", ex2.getMessage());

        verify(identityProvider, never()).currentIdentity(); // fails before asking for user
        verify(taskRepository, never()).save(any());
    }

    @Test
    void givingValidTaskWithInvalidUser_whenCreateTask_thenExceptionShouldThrown() {
        // given
        Task task = buildValidTask();

        // case 1: identityProvider returns null user
        when(identityProvider.currentIdentity()).thenReturn(null);

        IllegalStateException ex1 =
                assertThrows(IllegalStateException.class, () -> taskService.createTask(task));
        assertEquals("Current user not found", ex1.getMessage());
        verify(taskRepository, never()).save(any());

        // case 2: identityProvider returns user with null id
        reset(taskRepository, identityProvider);

        AppUser userWithNullId = new AppUser();
        userWithNullId.setId(null);
        when(identityProvider.currentIdentity()).thenReturn(userWithNullId);

        IllegalStateException ex2 =
                assertThrows(IllegalStateException.class, () -> taskService.createTask(task));
        assertEquals("Current user not found", ex2.getMessage());
        verify(taskRepository, never()).save(any());
    }

    @Test
    void givingValidTaskId_whenToggleTask_thenExceptionShouldBeThrownAndSavedToDb() {
        Long userId = buildValidUser().getId();
        Long taskId = 1L;
        Boolean isCompleted = true;

        Task task = new Task();
        task.setId(taskId);
        task.setIsCompleted(isCompleted);

        when(identityProvider.currentIdentity()).thenReturn(buildValidUser());
        when(taskRepository.getTaskByIdAndUserId(taskId, userId)).thenReturn(task);

        assertDoesNotThrow(() -> taskService.toggleTask(taskId));

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(1)).save(captor.capture());

        Task savedTask = captor.getValue();

        assertEquals(!isCompleted, savedTask.getIsCompleted());
    }


    private Task buildValidTask() {
        Task task = new Task();
        task.setTitle("test");
        task.setAppUserId(1L); // will be overridden by service anyway
        return task;
    }

    private AppUser buildValidUser() {
        AppUser user = new AppUser();
        user.setId(99L);
        return user;
    }

    private Task buildInvalidTask() {
        Task task = new Task();
        task.setTitle("");
        task.setAppUserId(1L);
        return task;
    }
}