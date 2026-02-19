package com.microservices.models.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasskeyRegistrationDTO {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    String email;
    
    String authenticatorName;
}
