package com.microservices.repositories;

import com.microservices.models.DAO.PasswordLoginDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordLoginRepository extends JpaRepository<PasswordLoginDAO, Long> {
    PasswordLoginDAO findByEmailAndPassword(String email, String password);
}
