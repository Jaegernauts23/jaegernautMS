package com.microservices.repositories;

import com.microservices.models.DAO.PasskeyCredentialDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasskeyCredentialRepository extends JpaRepository<PasskeyCredentialDAO, Long> {
    List<PasskeyCredentialDAO> findByEmail(String email);
    PasskeyCredentialDAO findByCredentialId(String credentialId);
}
