package com.microservices.services;

import com.microservices.models.OAuthClientEntity;
import com.microservices.repositories.OAuthClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OAuthRegistrationService{

    @Autowired
    OAuthClientRepository OAuthClientRepo;
    public OAuthClientEntity registerClient(String clientName,String redirectUri,String scope) {
        OAuthClientEntity clientEntry= OAuthClientEntity.builder()
                .clientName(clientName)
                .scope(scope)
                .redirectUri(redirectUri)
                .build();
        return OAuthClientRepo.save(clientEntry);
    }

    public boolean isValidClient(String clientId, String clientSecret) {
        return OAuthClientRepo.findByClientIdAndClientSecret(clientId, clientSecret).isPresent();
    }
    public OAuthClientEntity getClientDetails(String clientId) {
        return OAuthClientRepo.findByClientId(clientId).orElse(null);
    }

    public boolean isActiveClient(String clientId){
        return OAuthClientRepo.findByClientId(clientId).map(client -> client.isActive()).orElse(false);
    }
}