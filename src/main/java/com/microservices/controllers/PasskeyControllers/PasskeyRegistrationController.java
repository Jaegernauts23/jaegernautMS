package com.microservices.controllers.PasskeyControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservices.models.DTO.PasskeyRegistrationDTO;
import com.microservices.services.PasskeyServices.PasskeyRegistrationService;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/passkey")
public class PasskeyRegistrationController {
    
    @Autowired
    private PasskeyRegistrationService passkeyRegistrationService;
    
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        model.addAttribute("passkeyRegistrationDTO", new PasskeyRegistrationDTO());
        return "PasskeyFlowTemplates/PasskeyRegistration";
    }
    
    @PostMapping("/register/start")
    @ResponseBody
    public ResponseEntity<?> startRegistration(@Valid @RequestBody PasskeyRegistrationDTO dto) {
        PublicKeyCredentialCreationOptions options = passkeyRegistrationService.startRegistration(dto.getEmail());
        if (options == null) {
            return ResponseEntity.badRequest().body("User not found");
        }
        try {
            return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(options.toCredentialsCreateJson());
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("Error generating challenge");
        }
    }
    
    @PostMapping("/register/finish")
    @ResponseBody
    public ResponseEntity<?> finishRegistration(@RequestParam String email, 
                                                @RequestParam String credential,
                                                @RequestParam(required = false) String authenticatorName) {
        boolean success = passkeyRegistrationService.finishRegistration(email, credential, authenticatorName);
        if (success) {
            return ResponseEntity.ok("Passkey registered successfully");
        }
        return ResponseEntity.badRequest().body("Registration failed");
    }
}
