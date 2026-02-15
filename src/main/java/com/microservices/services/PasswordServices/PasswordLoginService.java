package com.microservices.services.PasswordServices;

import com.microservices.models.DAO.PasswordLoginDAO;
import com.microservices.repositories.PasswordLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PasswordLoginService {
    
    @Autowired
    PasswordLoginRepository passwordLoginRepository;
    
    public boolean verfiyValidLogin(PasswordLoginDAO passwordLoginDAO){
        PasswordLoginDAO user = passwordLoginRepository.findByEmailAndPassword(
            passwordLoginDAO.getEmail(), 
            passwordLoginDAO.getPassword()
        );
        return user != null;
    }
}
