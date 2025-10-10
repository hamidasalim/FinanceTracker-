package com.fintech.enterprise.controller;

import com.fintech.enterprise.model.Role;
import com.fintech.enterprise.model.User;
import com.fintech.enterprise.service.AuthService;
import com.fintech.enterprise.service.dto.LoginDTO;
import com.fintech.enterprise.service.dto.RegisterDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterDTO request) {
        User user = authService.register(request.getUsername(), request.getPassword(), request.getRole());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO request) {
        String token = authService.login(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
