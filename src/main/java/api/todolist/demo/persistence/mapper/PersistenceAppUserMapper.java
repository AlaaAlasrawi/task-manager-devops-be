package api.todolist.demo.persistence.mapper;

import api.todolist.demo.domain.model.AppUser;
import api.todolist.demo.persistence.entity.AppUserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersistenceAppUserMapper {

    AppUser toDomain(AppUserEntity appUserEntity);
    AppUserEntity toEntity(AppUser appUser);
}
