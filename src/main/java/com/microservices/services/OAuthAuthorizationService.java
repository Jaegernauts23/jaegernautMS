package com.microservices.services;

import com.microservices.models.AuthorizationCode;
import com.microservices.repositories.AuthorizationCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OAuthAuthorizationService {
    private final AuthorizationCodeRepository authCodeRepo;
    public String saveCode(String clientId,String scope,String redirectUri,String name){
       AuthorizationCode authCode = AuthorizationCode.builder()
               .username(name)
               .clientId(clientId)
               .scope(scope)
               .redirectUri(redirectUri)
               .build();
       AuthorizationCode authCodeStored = authCodeRepo.save(authCode);
       return authCodeStored.getCode();
    }

    public String validateAndConsumeToken(String tokenCode,String clientId,String redirectUri){
        return authCodeRepo.findByCodeAndUsed(tokenCode,false)
                .filter(authorizationCode -> authorizationCode.getExpiresAt().isAfter(LocalDateTime.now()))
                .filter(authorizationCode -> authorizationCode.getClientId().equals(clientId))
                .filter(authorizationCode -> authorizationCode.getRedirectUri().equals(redirectUri))
                .map(authorizationCode -> {
                    authorizationCode.setUsed(true);
                    authCodeRepo.save(authorizationCode);
                    return authorizationCode.getUsername();
                }
                ).orElse(null);
    }
}
