package com.microservices.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "authorization_codes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String code;
    private String clientId;
    private String redirectUri;
    private String scope;
    private boolean used;
    private LocalDateTime expiresAt;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @PrePersist
    public void generateCode() {
        if (code == null) {
            code = UUID.randomUUID().toString();
        }
        if (expiresAt == null) {
            expiresAt = createdAt.plusMinutes(10);
        }
    }
}
