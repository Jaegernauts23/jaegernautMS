package com.microservices.controllers.PasswordControllers;

import com.microservices.models.DTO.PasswordSignUpDTO;
import com.microservices.services.PasswordServices.PasswordSignUpService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/password")
public class PasswordSignUpController {

    @Autowired
    PasswordSignUpService passwordSignUpService;

    @GetMapping("/signup")
    public String showDisplayPage(Model model) {
        model.addAttribute("passwordSignUpDTO", new PasswordSignUpDTO());
        return "PasswordFlowTemplates/PasswordSignUp";
    }

    @PostMapping("/signup")
    public String registerUser(@Valid @ModelAttribute PasswordSignUpDTO passwordSignUpDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "PasswordFlowTemplates/PasswordSignUp";
        }
        if (passwordSignUpService.registerUser(passwordSignUpDTO)) {
            return "redirect:/password/login";
        }
        model.addAttribute("error", "Email already exists");
        return "PasswordFlowTemplates/PasswordSignUp";
    }
}
