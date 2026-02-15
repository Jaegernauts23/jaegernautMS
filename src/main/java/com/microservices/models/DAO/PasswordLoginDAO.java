package com.microservices.models.DAO;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


@Entity
@Data
public class PasswordLoginDAO {

    @Id
    Long id;
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    String email;
    String password;
}
