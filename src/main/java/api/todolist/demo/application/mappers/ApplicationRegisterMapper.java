package api.todolist.demo.application.mappers;

import api.todolist.demo.application.dtos.register.RegisterRequest;
import api.todolist.demo.application.dtos.register.RegisterResponse;
import api.todolist.demo.domain.model.AppUser;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ApplicationRegisterMapper {

    AppUser requestToModel(RegisterRequest registerRequest);

    RegisterResponse modelToResponse(AppUser user);
}
