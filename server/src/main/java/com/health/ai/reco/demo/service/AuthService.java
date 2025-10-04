package com.health.ai.reco.demo.service;

import com.health.ai.reco.demo.model.dto.login.LoginRequest;
import com.health.ai.reco.demo.model.dto.login.RegisterRequest;
import com.health.ai.reco.demo.model.dto.login.TokenResponse;
import com.health.ai.reco.demo.model.entity.UserProfileEntity;
import com.health.ai.reco.demo.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author M_Khandan
 * Date: 6/10/2025
 * Time: 4:58 PM
 */
@Service
public class AuthService {

    @Autowired
    private UserProfileRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtService jwtService;

    public void register(RegisterRequest request) {
        if (userRepo.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        UserProfileEntity user = new UserProfileEntity();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepo.save(user);
    }

    public TokenResponse login(LoginRequest request) {
        UserProfileEntity user = userRepo.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new RuntimeException("Invalid credentials");

        String token = jwtService.generateToken(user);
        return new TokenResponse(token);
    }
}

