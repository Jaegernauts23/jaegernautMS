package com.microservices.models.DTO;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordLoginDTO {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    String email;
    String password;
}
