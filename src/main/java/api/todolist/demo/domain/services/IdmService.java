package api.todolist.demo.domain.services;

import api.todolist.demo.application.exception.DuplicateResourceException;
import api.todolist.demo.domain.model.AppUser;
import api.todolist.demo.domain.providers.IdentityProvider;
import api.todolist.demo.domain.repository.AppUserRepository;
import api.todolist.demo.domain.services.security.AppUserDetailsService;
import api.todolist.demo.domain.services.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class IdmService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService appUserDetailsService;
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdentityProvider identityProvider;

    public AppUser register(AppUser user) {
        validateUserInfo(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        fillUpMissingInfo(user);
        return appUserRepository.save(user);
    }

    private void validateUserInfo(AppUser user) {

        if (appUserRepository.isUsernameAlreadyExists(user.getUsername())) {
            throw new DuplicateResourceException("Username already exists");
        }

        if (appUserRepository.isEmailAlreadyExists(user.getEmail())) {
            throw new DuplicateResourceException("email already exists");
        }
    }

    private void fillUpMissingInfo(AppUser user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
    }
}
