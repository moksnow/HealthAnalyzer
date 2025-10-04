package com.health.ai.reco.demo.controller;

import com.health.ai.reco.demo.model.dto.login.LoginRequest;
import com.health.ai.reco.demo.model.dto.login.RegisterRequest;
import com.health.ai.reco.demo.model.dto.login.TokenResponse;
import com.health.ai.reco.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author M_Khandan
 * Date: 6/10/2025
 * Time: 4:57 PM
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {


    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        authService.register(request);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
