package com.microservices.controllers;

import com.microservices.services.JwtTokenService;
import com.microservices.models.UserEntity;
import com.microservices.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProtectedResourceController {

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {

        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "missing_token"));
        }

        String token = authHeader.substring(7); // Remove "Bearer "

        if (!jwtTokenService.validateToken(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_token"));
        }

        String username = jwtTokenService.getUsernameFromToken(token);
        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            return ResponseEntity.status(404).body(Map.of("error", "user_not_found"));
        }

        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "userpass",user.getPassword(),
                "message", "Access granted via OAuth token!"
        ));
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String authHeader) {

        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "missing_token"));
        }

        String token = authHeader.substring(7);

        if (!jwtTokenService.validateToken(token)) {
            return ResponseEntity.status(401).body(Map.of("error", "invalid_token"));
        }

        String username = jwtTokenService.getUsernameFromToken(token);
        String clientId = jwtTokenService.getClientIdFromToken(token);

        return ResponseEntity.ok(Map.of(
                "username", username,
                "client_id", clientId,
                "profile_data", "User profile information here",
                "permissions", "read, write"
        ));
    }
}
