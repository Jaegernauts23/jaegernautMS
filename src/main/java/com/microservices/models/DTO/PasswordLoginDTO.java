package com.microservices.models.DTO;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PasswordLoginDTO {
    @Pattern(regexp = "^[a-zA-Z0-9]{5,15}$",
            message = "Username must be 5-15 characters and alphanumeric")
    String email;
    String password;
}
