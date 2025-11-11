package com.microservices.repositories;

import com.microservices.models.UserPasskeyEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface UserPasskeyRepository  extends JpaRepository<UserPasskeyEntity,Long> {
    UserPasskeyEntity findByCredentialId(String credentialId);
    // Add to UserPasskeyRepository.java
    List<UserPasskeyEntity> findByUsername(String username);
}
