package api.todolist.demo.persistence.adapter;

import api.todolist.demo.domain.model.Task;
import api.todolist.demo.domain.repository.TaskRepository;
import api.todolist.demo.persistence.entity.TaskEntity;
import api.todolist.demo.persistence.jpa.TaskJpaRepository;
import api.todolist.demo.persistence.mapper.PersistenceTaskMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TaskAdapter implements TaskRepository {
    private final TaskJpaRepository taskJpaRepository;
    private final PersistenceTaskMapper taskMapper;

    @Override
    public void save(Task task) {
        taskJpaRepository.save(taskMapper.toEntity(task));
    }


    @Override
    public void updateTaskById(Long id, Task task) {
        taskJpaRepository.updateTaskById(id, taskMapper.toEntity(task));
    }

    @Override
    public Page<Task> getAllTasksByUserId( Long appUserId, int page, int size, String sortBy, String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<TaskEntity> entities = taskJpaRepository.findAll( pageable);

        List<Task> tasks = entities.getContent()
                .stream()
                .map(taskMapper::toDomain)
                .toList();

        return new PageImpl<>(tasks, pageable, entities.getTotalElements());
    }

    @Override
    public void deleteAllTasksByUserId(Long userId) {
        taskJpaRepository.deleteAllByAppUser_Id(userId);
    }

    @Override
    public void deleteCompletedTasksByUserId(Long userId) {
        taskJpaRepository.deleteAllByAppUser_IdAndIsCompletedTrue(userId);
    }

    @Override
    public void deleteTasksByIdsAndUserId(List<Long> ids, Long userId) {
        taskJpaRepository.deleteAllByIdInAndAppUser_Id(ids, userId);
    }

    @Override
    public Task getTaskByIdAndUserId(Long id, Long userId) {
        return taskMapper.toDomain(taskJpaRepository.findByIdAndAppUser_Id(id, userId).orElseThrow(
                () -> new RuntimeException("Task not found: " + id)
        ));
    }


}
