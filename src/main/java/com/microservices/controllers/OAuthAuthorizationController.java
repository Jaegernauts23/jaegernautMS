package com.microservices.controllers;

import com.microservices.models.OAuthClientEntity;
import com.microservices.services.OAuthRegistrationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/oauth2")
@RequiredArgsConstructor
public class OAuthAuthorizationController {
    private final OAuthRegistrationService clientService;

    @GetMapping("/authorize")
    public String authorize(@RequestParam(name = "clientId") String clientId, @RequestParam(name = "redirectUri") String redirectUri, @RequestParam(name = "responseType") String responseType, @RequestParam(name = "scope") String scope, Model model, HttpSession session){
    OAuthClientEntity registerDBClient = clientService.getClientDetails(clientId);
    if(registerDBClient==null || !registerDBClient.isActive()){
        return "error";
    }
    if(!registerDBClient.getRedirectUri().equals(redirectUri)){
        return "error";
    }

    session.setAttribute("oauth_client_id",clientId);
    session.setAttribute("oauth_redirect_uri",redirectUri);
    session.setAttribute("oauth_scope",scope);

    model.addAttribute("oauth_mode",true);
    model.addAttribute("client_name",registerDBClient.getClientName());

    return "passwordscreen";
    }

    @PostMapping("/authorize")
    public String authorizePostMethod(Model model,HttpSession session){
        session.setAttribute("oauth_login_attempt",true);
        return "forward:/home";
    }
}
