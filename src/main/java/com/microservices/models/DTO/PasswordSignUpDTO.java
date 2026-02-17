package com.microservices.models.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import java.time.LocalDate;

@Data
public class PasswordSignUpDTO {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email format")
    String email;
    
    @NotBlank(message = "Name cannot be empty")
    String name;
    
    @NotNull(message = "Date of birth is required")
    LocalDate dob;
    
    @NotBlank(message = "Password cannot be empty")
    String password;
    
    @NotBlank(message = "Role cannot be empty")
    String role;
}
