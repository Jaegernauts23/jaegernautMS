package com.microservices.repositories;

import com.microservices.models.DAO.UserDetailsDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsRepository extends JpaRepository<UserDetailsDAO, Long> {
    UserDetailsDAO findByEmail(String email);
}
