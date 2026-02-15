package com.microservices.services.PasswordServices;

import com.microservices.models.DAO.PasswordLoginDAO;
import org.springframework.stereotype.Service;

@Service
public class PasswordLoginService {
    public boolean verfiyValidLogin(PasswordLoginDAO passwordLoginDAO){
        return false;
    }
}
