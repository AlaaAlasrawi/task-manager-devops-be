package api.todolist.demo.application.controllers;

import api.todolist.demo.application.dtos.task.TaskRequest;
import api.todolist.demo.application.mappers.ApplicationTaskMapper;
import api.todolist.demo.application.rest.specs.TaskSpecs;
import api.todolist.demo.domain.model.Task;
import api.todolist.demo.domain.services.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final ApplicationTaskMapper applicationTaskMapper;

    @PostMapping
    public ResponseEntity<Void> createTask(@RequestBody TaskRequest request) {
        taskService.createTask(applicationTaskMapper.toDomain(request));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTaskById(@PathVariable("id") Long id, @RequestBody TaskRequest request) {
        taskService.updateTaskById(id, applicationTaskMapper.toDomain(request));
        return ResponseEntity.noContent().build(); // 204
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    // to do (specification)
    @GetMapping
    public ResponseEntity<Page<Task>> getAllTasks(
            TaskSpecs specs,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "size") int size,
            @RequestParam(defaultValue = "title", name = "sortBy") String sortBy,
            @RequestParam(defaultValue = "asc", name = "sortDirection") String sortDirection) {
        return ResponseEntity.ok(taskService.getAllTasks(specs, page, size, sortBy, sortDirection));
    }

    @PatchMapping("/toggle/{id}")
    public ResponseEntity<Boolean> toggleTask(@PathVariable("id") Long id) {
        boolean newValue = taskService.toggleTask(id);
        return ResponseEntity.ok(newValue);
    }

    @DeleteMapping("/selected")
    public ResponseEntity<Void> deleteSelectedTasksId(@RequestBody List<Long> ids) {
        taskService.deleteSelectedTasksId(ids);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllTasks() {
        taskService.deleteAllTasks();
        return ResponseEntity.noContent().build(); // 204
    }

    @DeleteMapping("/completed")
    public ResponseEntity<Void> deleteCompletedTasks() {
        taskService.deleteCompletedTasks();
        return ResponseEntity.noContent().build();
    }
}
