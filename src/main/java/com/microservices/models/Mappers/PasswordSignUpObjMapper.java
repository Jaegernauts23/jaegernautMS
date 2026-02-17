package com.microservices.models.Mappers;

import com.microservices.models.DAO.PasswordLoginDAO;
import com.microservices.models.DAO.UserDetailsDAO;
import com.microservices.models.DTO.PasswordSignUpDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PasswordSignUpObjMapper {

    @Mapping(target = "id", ignore = true)
    PasswordLoginDAO toPasswordLoginDAO(PasswordSignUpDTO passwordSignUpDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "lastUpdatedOn", ignore = true)
    UserDetailsDAO toUserDetailsDAO(PasswordSignUpDTO passwordSignUpDTO);
}
