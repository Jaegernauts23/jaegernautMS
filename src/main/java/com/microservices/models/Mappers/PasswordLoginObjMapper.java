package com.microservices.models.Mappers;

import com.microservices.models.DAO.PasswordLoginDAO;
import com.microservices.models.DTO.PasswordLoginDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PasswordLoginObjMapper {

    @Mapping(source = "passwordLoginDTO", target = "passwordLoginDAO")
    PasswordLoginDTO toPasswordLoginDTO(PasswordLoginDAO passwordLoginDAO);

    @Mapping(source = "passwordLoginDTO", target = "passwordLoginDAO")
    PasswordLoginDAO toPasswordLoginDAO(PasswordLoginDTO passwordLoginDTO);
}
