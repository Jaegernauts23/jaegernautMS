package com.microservices.configurations;

import com.microservices.repositories.WebAuthnCredentialRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebAuthnConfig {
    
    @Bean
    public RelyingParty relyingParty(WebAuthnCredentialRepository credentialRepository) {
        RelyingPartyIdentity rpIdentity = RelyingPartyIdentity.builder()
            .id("localhost")
            .name("JaugernautMS")
            .build();
        
        return RelyingParty.builder()
            .identity(rpIdentity)
            .credentialRepository(credentialRepository)
            .origins(java.util.Set.of("http://localhost:8080"))  // Allow this origin
            .build();
    }
}
