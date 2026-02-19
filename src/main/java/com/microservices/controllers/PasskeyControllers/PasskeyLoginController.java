package com.microservices.controllers.PasskeyControllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.microservices.JWTservices.JwtTokenService;
import com.microservices.services.PasskeyServices.PasskeyLoginService;
import com.yubico.webauthn.data.PublicKeyCredentialRequestOptions;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/passkey")
public class PasskeyLoginController {
    
    @Autowired
    private PasskeyLoginService passkeyLoginService;
    
    @Autowired
    private JwtTokenService jwtTokenService;
    
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "PasskeyFlowTemplates/PasskeyLogin";
    }
    
    @PostMapping("/login/start")
    @ResponseBody
    public ResponseEntity<?> startLogin() {
        try {

            PublicKeyCredentialRequestOptions options = passkeyLoginService.startAuthentication();

            String json = options.toCredentialsGetJson();
            System.out.println("JSON: " + json);
            return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(json);
        } catch (Exception e) {
            System.err.println("Error in startLogin: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }
    
    @PostMapping("/login/finish")
    @ResponseBody
    public ResponseEntity<?> finishLogin(@RequestParam String credential, HttpSession session, Model model) {
        String email = passkeyLoginService.finishAuthentication(credential);
        
        if (email != null) {
            String token = jwtTokenService.generateToken(email);
            session.setAttribute("jwt_token", token);
            model.addAttribute("token", token);
            return ResponseEntity.ok().body("Login successful");
        }
        
        return ResponseEntity.badRequest().body("Authentication failed");
    }
}
