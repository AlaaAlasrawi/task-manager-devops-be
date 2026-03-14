package api.todolist.demo.domain.repository;

import api.todolist.demo.domain.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository {
    void save(Task task);


    void updateTaskById(Long id, Task task);

    Page<Task> getAllTasksByUserId(Long appUserId, int page, int size, String sortBy, String sortDirection);

    void deleteAllTasksByUserId(Long id);

    void deleteCompletedTasksByUserId(Long userId);

    void deleteTasksByIdsAndUserId(List<Long> ids, Long userId);

    Task getTaskByIdAndUserId(Long id, Long userId);

}
