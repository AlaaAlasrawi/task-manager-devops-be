package api.todolist.demo.application.controllers;

import api.todolist.demo.application.dtos.authentication.UserAuthenticationRequest;
import api.todolist.demo.application.dtos.authentication.UserAuthenticationResponse;
import api.todolist.demo.application.dtos.register.RegisterRequest;
import api.todolist.demo.application.dtos.register.RegisterResponse;
import api.todolist.demo.application.mappers.ApplicationRegisterMapper;
import api.todolist.demo.application.mappers.UserAuthenticationMapper;
import api.todolist.demo.domain.model.AppUser;
import api.todolist.demo.domain.services.IdmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/idm")
@RequiredArgsConstructor
public class IdmController {
    private final IdmService idmService;
    private final ApplicationRegisterMapper registerMapper;
    private final UserAuthenticationMapper userAuthenticationMapper;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(registerMapper.modelToResponse(idmService.register(registerMapper.requestToModel(request))));
    }

    @PostMapping("/login")
    public ResponseEntity<UserAuthenticationResponse> login(@RequestBody UserAuthenticationRequest request) {
        return ResponseEntity.ok(userAuthenticationMapper.modelToResponse(idmService.login(userAuthenticationMapper.requestToModel(request))));
    }

    @GetMapping("/{username}")
    public ResponseEntity<AppUser> getUserByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(idmService.getUserByUsername(username));
    }
}

//docker exec -it postgres psql -U root -d postgres
// \c todolistdb
