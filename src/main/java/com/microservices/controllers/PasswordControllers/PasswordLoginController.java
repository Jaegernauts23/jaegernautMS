package com.microservices.controllers.PasswordControllers;

import com.microservices.JWTservices.JwtTokenService;
import com.microservices.models.DTO.PasswordLoginDTO;
import com.microservices.models.Mappers.PasswordLoginObjMapper;

import com.microservices.services.PasswordServices.PasswordLoginService;
import jakarta.servlet.http.HttpSession;
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
public class PasswordLoginController {

    @Autowired
    PasswordLoginService passwordLoginService;
    @Autowired
    PasswordLoginObjMapper passwordLoginObjMapper;
    @Autowired
    JwtTokenService jwtTokenService;

    @GetMapping("/login")
    public String showDisplayPage(Model model){
        model.addAttribute("passwordLoginDTO", new PasswordLoginDTO());
        return "PasswordFlowTemplates/PasswordLogin";
    }

    @PostMapping("/login")
    public String verifyLogin(@Valid @ModelAttribute PasswordLoginDTO passwordLoginDTO, BindingResult bindingResult, Model model, HttpSession session){
        if(bindingResult.hasErrors()){
            return "PasswordFlowTemplates/PasswordLogin";
        }
        if(passwordLoginService.verfiyValidLogin(passwordLoginObjMapper.toPasswordLoginDAO(passwordLoginDTO))){
            String token = jwtTokenService.generateToken(passwordLoginDTO.getEmail());
            session.setAttribute("jwt_token", token);
            model.addAttribute("token", token);
            return "JWTTemplates/TokenDisplay";
        }
        model.addAttribute("error", "Invalid credentials");
        return "PasswordFlowTemplates/PasswordLogin";
    }
}
