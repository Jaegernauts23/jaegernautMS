package com.microservices.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.microservices.models.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long>{
    UserEntity findByUsernameAndPassword(String username, String password);
    UserEntity findByUsername(String username);
}
