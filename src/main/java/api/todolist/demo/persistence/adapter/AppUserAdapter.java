package api.todolist.demo.persistence.adapter;

import api.todolist.demo.domain.model.AppUser;
import api.todolist.demo.domain.repository.AppUserRepository;
import api.todolist.demo.persistence.entity.AppUserEntity;
import api.todolist.demo.persistence.jpa.AppUserJpaRepository;
import api.todolist.demo.persistence.mapper.PersistenceAppUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AppUserAdapter implements AppUserRepository {
    private final AppUserJpaRepository appUserJpaRepository;
    private final PersistenceAppUserMapper persistenceAppUserMapper;

    @Override
    public AppUser getAppUserById(Long id) {
        return persistenceAppUserMapper.toDomain(appUserJpaRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("user with this id do not exist id = " + id)
                ));
    }

    @Override
    public AppUser findByUsername(String username) {
        AppUserEntity entity = appUserJpaRepository.findByUsername(username)
                .orElseThrow(
                        () -> new RuntimeException("user with this username does not exist username = " + username)
                );
        return persistenceAppUserMapper.toDomain(entity);
    }

    @Override
    public AppUser save(AppUser user) {
        return persistenceAppUserMapper.toDomain(appUserJpaRepository.save(persistenceAppUserMapper.toEntity(user)));
    }

    @Override
    public boolean isUsernameAlreadyExists(String username) {
        return appUserJpaRepository.existsByUsername(username);
    }

    @Override
    public boolean isEmailAlreadyExists(String email) {
        return appUserJpaRepository.existsByEmail(email);
    }
}
