package com.microservices.repositories;

import com.microservices.models.AuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode,Long> {
    Optional<AuthorizationCode> findByCodeAndUsed(String code,boolean used);
}
