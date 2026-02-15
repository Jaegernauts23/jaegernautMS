package com.microservices.controllers.PasswordControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/password")
public class PasswordSignUpController {
    @GetMapping("/signUp")
    public String showDisplayPage() {
        return "PasswordFlowTemplates/PasswordLogin";
    }
}
