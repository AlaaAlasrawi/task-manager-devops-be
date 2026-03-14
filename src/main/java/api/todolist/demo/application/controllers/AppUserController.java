package api.todolist.demo.application.controllers;

import api.todolist.demo.domain.model.AppUser;
import api.todolist.demo.domain.services.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appuser")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(appUserService.getAppUserById(id));
    }
}
