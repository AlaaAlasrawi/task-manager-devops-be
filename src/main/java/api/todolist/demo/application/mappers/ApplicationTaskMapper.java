package api.todolist.demo.application.mappers;


import api.todolist.demo.application.dtos.task.TaskRequest;
import api.todolist.demo.domain.model.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationTaskMapper {
    Task toDomain(TaskRequest request);
}
