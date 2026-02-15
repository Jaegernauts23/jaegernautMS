package com.microservices.controllers.ProtectedResourceControllers;


import com.microservices.JWTservices.JwtTokenService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
public class ProtectedResourceController {

    @Autowired
    JwtTokenService jwtTokenService;

    @GetMapping("/secret")
    public String accessProtectedResource(@RequestHeader(value = "Authorization", required = false) String authHeader, HttpSession session, Model model) {
        try {
            String token = null;
            if (authHeader != null) {
                token = authHeader.replace("Bearer ", "");
            } else {
                token = (String) session.getAttribute("jwt_token");
            }
            String email = jwtTokenService.validateToken(token);
            model.addAttribute("message", "Welcome, You now can see the protected Resource");
            model.addAttribute("email", email);
            return "JWTTemplates/SecretPage";
        } catch (Exception e) {
            model.addAttribute("error", "Unauthorized: Invalid token");
            return "error";
        }
    }
}
