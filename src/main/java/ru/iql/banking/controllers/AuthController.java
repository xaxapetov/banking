package ru.iql.banking.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.iql.banking.dtos.auth.AuthRequest;
import ru.iql.banking.dtos.auth.AuthResponse;
import ru.iql.banking.services.impl.AuthServiceImpl;

@RestController
@RequestMapping("/banking/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authServiceImpl;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody AuthRequest request){
        return ResponseEntity.ok(authServiceImpl.authenticate(request));
    }
}
