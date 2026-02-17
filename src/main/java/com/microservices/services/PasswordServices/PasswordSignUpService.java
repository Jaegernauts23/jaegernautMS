package com.microservices.services.PasswordServices;

import com.microservices.models.DAO.PasswordLoginDAO;
import com.microservices.models.DAO.UserDetailsDAO;
import com.microservices.models.DTO.PasswordSignUpDTO;
import com.microservices.models.Mappers.PasswordSignUpObjMapper;
import com.microservices.repositories.PasswordLoginRepository;
import com.microservices.repositories.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PasswordSignUpService {

    @Autowired
    PasswordLoginRepository passwordLoginRepository;

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    PasswordSignUpObjMapper passwordSignUpObjMapper;

    @Transactional
    public boolean registerUser(PasswordSignUpDTO passwordSignUpDTO) {
        if (userDetailsRepository.findByEmail(passwordSignUpDTO.getEmail()) != null) {
            return false;
        }

        PasswordLoginDAO passwordLoginDAO = passwordSignUpObjMapper.toPasswordLoginDAO(passwordSignUpDTO);
        passwordLoginRepository.save(passwordLoginDAO);

        UserDetailsDAO userDetailsDAO = passwordSignUpObjMapper.toUserDetailsDAO(passwordSignUpDTO);
        userDetailsRepository.save(userDetailsDAO);

        return true;
    }
}
