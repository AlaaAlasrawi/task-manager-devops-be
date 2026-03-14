package api.todolist.demo.domain.services;

import api.todolist.demo.domain.model.AppUser;
import api.todolist.demo.domain.model.Task;
import api.todolist.demo.domain.providers.IdentityProvider;
import api.todolist.demo.domain.repository.AppUserRepository;
import api.todolist.demo.domain.repository.TaskRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {
    private final TaskRepository taskRepository;
    private final IdentityProvider identityProvider;

    public void createTask(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task is required");
        }

        if (task.getTitle() == null || task.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Task title is required");
        }

        AppUser appUser = identityProvider.currentIdentity();
        if (appUser == null || appUser.getId() == null) {
            throw new IllegalStateException("Current user not found");
        }

        task.setAppUserId(appUser.getId());
        task.setIsCompleted(false);

        LocalDateTime now = LocalDateTime.now();
        task.setCreatedAt(now);
        task.setUpdatedAt(now);

        taskRepository.save(task);
    }

    public Task getTaskById(Long id) {
        AppUser user = identityProvider.currentIdentity();
        return taskRepository.getTaskByIdAndUserId(id, user.getId());
    }

    public void updateTaskById(Long id, Task task) {
        if (task.getTitle() != null) {
            String title = task.getTitle().trim();
            if (title.isEmpty()) {
                throw new IllegalArgumentException("title must not be blank");
            }
            task.setTitle(title);
        }

        task.setUpdatedAt(LocalDateTime.now());

        AppUser appUser = identityProvider.currentIdentity(); // 99
        Task existing = taskRepository.getTaskByIdAndUserId(id, appUser.getId()); // 1, 99

        if (task.getTitle() != null) existing.setTitle(task.getTitle());
        if (task.getIsCompleted() != null) existing.setIsCompleted(task.getIsCompleted());
        existing.setUpdatedAt(task.getUpdatedAt());
        taskRepository.save(existing);
    }

    public Page<Task> getAllTasks(Specification<?> specs, int page, int size, String sortBy, String sortDirection) {
        AppUser appUser = identityProvider.currentIdentity();

        return taskRepository.getAllTasksByUserId(specs, appUser.getId(), page, size, sortBy, sortDirection);
    }

    public void deleteAllTasks() {
        AppUser appUser = identityProvider.currentIdentity();
        taskRepository.deleteAllTasksByUserId(appUser.getId());
    }

    public void deleteCompletedTasks() {
        AppUser appUser = identityProvider.currentIdentity();
        taskRepository.deleteCompletedTasksByUserId(appUser.getId());
    }

    public boolean toggleTask(Long id) {
        AppUser appUser = identityProvider.currentIdentity();
        Task task = taskRepository.getTaskByIdAndUserId(id, appUser.getId());

        boolean newValue = !Boolean.TRUE.equals(task.getIsCompleted());
        task.setIsCompleted(newValue);

        task.setUpdatedAt(LocalDateTime.now());
        taskRepository.save(task);

        return newValue;
    }

    public void deleteSelectedTasksId(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return;

        AppUser appUser = identityProvider.currentIdentity();
        taskRepository.deleteTasksByIdsAndUserId(ids, appUser.getId());
    }
}
