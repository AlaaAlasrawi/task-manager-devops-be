package api.todolist.demo.application.mappers;

import api.todolist.demo.application.dtos.authentication.UserAuthenticationRequest;
import api.todolist.demo.application.dtos.authentication.UserAuthenticationResponse;
import api.todolist.demo.domain.model.UserAuthentication;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAuthenticationMapper {
    UserAuthentication requestToModel(UserAuthenticationRequest userAuthenticationRequest);
    UserAuthenticationResponse modelToResponse(UserAuthentication userAuthentication);
}
