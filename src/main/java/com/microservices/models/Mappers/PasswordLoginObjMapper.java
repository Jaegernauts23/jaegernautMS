package com.microservices.models.Mappers;

import com.microservices.models.DAO.PasswordLoginDAO;
import com.microservices.models.DTO.PasswordLoginDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PasswordLoginObjMapper {

    PasswordLoginDTO toPasswordLoginDTO(PasswordLoginDAO passwordLoginDAO);

    PasswordLoginDAO toPasswordLoginDAO(PasswordLoginDTO passwordLoginDTO);
}
