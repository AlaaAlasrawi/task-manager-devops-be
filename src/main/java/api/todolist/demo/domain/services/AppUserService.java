package api.todolist.demo.domain.services;

import api.todolist.demo.domain.model.AppUser;
import api.todolist.demo.domain.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;

    public AppUser getAppUserById(Long id) {
        return appUserRepository.getAppUserById(id);
    }
}
