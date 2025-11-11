package com.microservices.configurations;

import com.microservices.repositories.UserpasskeyCredentialRepository;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.data.RelyingPartyIdentity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class PasskeyConfiguration {
    @Bean
    public RelyingParty relyingParty(UserpasskeyCredentialRepository userPasskeyCredentialRepository){
        RelyingPartyIdentity ri = RelyingPartyIdentity.builder()
                .id("localhost")
                .name("JaegernuatMS")
                .build();

        return RelyingParty.builder()
                .identity(ri)
                .credentialRepository(userPasskeyCredentialRepository)
                .origins(Set.of("http://localhost:8080"))
                .build();
    }
}
