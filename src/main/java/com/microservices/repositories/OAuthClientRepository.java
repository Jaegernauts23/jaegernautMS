package com.microservices.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.models.OAuthClientEntity;

public interface OAuthClientRepository extends JpaRepository<OAuthClientEntity,Long>{
    Optional<OAuthClientEntity> findByClientId(String clientId);
    Optional<OAuthClientEntity> findByClientIdAndClientSecret(String clientId, String clientSecret);
}
