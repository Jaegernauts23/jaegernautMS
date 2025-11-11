package com.microservices.controllers;

import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.exception.Base64UrlException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/passkey/register/")
@Controller
public class PasskeyRegistrationController {

    @Autowired
    WebAuthnService  webAuthentication;

    @GetMapping("registerpage")
    public String registerPage() {
        return "register";
    }


    @PostMapping("/start")
    @ResponseBody
    public ResponseEntity<PublicKeyCredentialCreationOptions> startPasskeyRegistration(@RequestParam String username) throws Base64UrlException {
        try {
            PublicKeyCredentialCreationOptions result = webAuthentication.startRegistration(username);
            return  ResponseEntity.ok(result);
        }
        catch (Exception e){
            return  ResponseEntity.badRequest().build();
        }

    }
    @PostMapping("/finish")
    @ResponseBody
    public ResponseEntity<String> finishPasskeyRegistration(@RequestParam String username,
                                                            @RequestBody PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> credential){
        System.out.println("Controller /finish endpoint called for user: " + username);
        try {
            webAuthentication.finishRegistration(username, credential);
            System.out.println("Controller: Registration completed successfully");
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            System.out.println("Controller ERROR: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Registration Failed: " + e.getMessage());
        }
    }
}
