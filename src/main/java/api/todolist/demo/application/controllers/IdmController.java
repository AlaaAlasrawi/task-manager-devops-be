package api.todolist.demo.application.controllers;

import api.todolist.demo.application.dtos.register.RegisterRequest;
import api.todolist.demo.application.dtos.register.RegisterResponse;
import api.todolist.demo.application.mappers.ApplicationRegisterMapper;
import api.todolist.demo.domain.services.IdmService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/idm")
@RequiredArgsConstructor
public class IdmController {
    private final IdmService idmService;
    private final ApplicationRegisterMapper registerMapper;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(registerMapper.modelToResponse(idmService.register(registerMapper.requestToModel(request))));
    }

}
