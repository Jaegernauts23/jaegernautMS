package com.microservices.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "oauth_clients")
@Data                      
@NoArgsConstructor          
@AllArgsConstructor
@Builder
public class OAuthClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable=false, unique=true)
    String clientId;
    @Column(nullable=false, unique=true)
    String clientSecret;
    @Column(nullable=false)
    String clientName;
    @Column(nullable=false)
    String redirectUri;
    @Column(nullable=false)
    String scope;
    @Builder.Default
    boolean active = true;

    @PrePersist
    public void generateClientCredentials() {
        if (clientId == null) {
            clientId = UUID.randomUUID().toString();
        }
        if (clientSecret == null) {
            clientSecret = UUID.randomUUID().toString();
        }
    }

}
