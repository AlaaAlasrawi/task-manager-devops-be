package api.todolist.demo.domain.repository;

import api.todolist.demo.domain.model.AppUser;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository {
    AppUser getAppUserById(Long id);

    AppUser findByUsername(String username);
}
