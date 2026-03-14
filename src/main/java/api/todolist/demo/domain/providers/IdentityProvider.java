package api.todolist.demo.domain.providers;

import api.todolist.demo.domain.model.AppUser;
import api.todolist.demo.domain.repository.AppUserRepository;
import api.todolist.demo.domain.services.security.AppUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IdentityProvider {
    private final AppUserRepository appUserRepository;
    private final AppUserDetailsService appUserDetailsService;

    public AppUser currentIdentity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            if(authentication.getPrincipal() instanceof UserDetails userDetails) {
                return appUserRepository.findByUsername(userDetails.getUsername());
            }

            if(authentication.getPrincipal() instanceof String username) {
                return appUserRepository.findByUsername(username);
            }
        }

        return null;
    }

    public UserDetails currentIdentityDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.isAuthenticated()) {
            if(authentication.getPrincipal() instanceof UserDetails userDetails) {
                return userDetails;
            }

            if(authentication.getPrincipal() instanceof String username) {
                return appUserDetailsService.loadUserByUsername(username);
            }
        }

        return null;
    }
}
