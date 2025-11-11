package com.microservices.controllers;

import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.data.PublicKeyCredentialRequestOptions;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/passkey/login/")
public class PasskeyLoginControllers {

    @Autowired
    WebAuthnService webAuthentication;
    // Add to PasskeyRegistrationController.java
    @PostMapping("/authenticate/start")
    @ResponseBody
    public ResponseEntity<PublicKeyCredentialRequestOptions> startAuthentication(@RequestParam String username) {
        try {

            PublicKeyCredentialRequestOptions options = webAuthentication.startAuthentication(username);
            return ResponseEntity.ok(options);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/authenticate/finish")
    @ResponseBody
    public ResponseEntity<String> finishAuthentication(
            @RequestParam String username,
            @RequestBody PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> credential,
            HttpSession session) {
        try {
            boolean success = webAuthentication.finishAuthentication(username, credential);
            if (success) {
                session.setAttribute("username", username);  // Login user
                return ResponseEntity.ok("Login successful");
            }
            return ResponseEntity.badRequest().body("Login failed");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Authentication failed");
        }
    }

    @GetMapping("loginpage")
    public String loginPage() {
        return "login";
    }

}
