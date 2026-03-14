package api.todolist.demo.persistence.mapper;

import api.todolist.demo.domain.model.Task;
import api.todolist.demo.persistence.entity.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersistenceTaskMapper {
    @Mapping(source = "appUserId", target = "appUser.id")
    TaskEntity toEntity(Task task);
    @Mapping(target = "appUserId", source = "appUser.id")
    Task toDomain(TaskEntity entity);
}
