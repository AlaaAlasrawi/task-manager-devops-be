package api.todolist.demo.domain.providers;

import api.todolist.demo.domain.model.AppUser;
import api.todolist.demo.domain.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IdentityProvider {
    private final AppUserRepository appUserRepository;

    public AppUser currentIdentity(){
        return appUserRepository.getAppUserById(2L);
    }
}
