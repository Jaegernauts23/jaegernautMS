package com.microservices.models.DAO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Entity
@Data
public class PasswordLoginDAO {

    @Id
    Long id;
    @Pattern(regexp = "^[a-zA-Z0-9]{5,15}$",
            message = "Username must be 5-15 characters and alphanumeric")
    String email;
    String password;
}
