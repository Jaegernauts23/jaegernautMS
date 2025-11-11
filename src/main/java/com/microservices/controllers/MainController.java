package com.microservices.controllers;

import com.microservices.services.OAuthAuthorizationService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import com.microservices.models.UserEntity;
import com.microservices.repositories.UserRepository;

@Controller
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OAuthAuthorizationService oAuthService;

    @GetMapping("/")
    public String showPasswordPage(HttpSession session,Model model) {
        logger.info("class {} :: request type {} :: path {}",this.getClass().toString(),"get","/");

        // Check if user is already logged in via passkey
        String username = (String) session.getAttribute("username");
        if (username != null) {
            model.addAttribute("username", username);
            return "Dashboardscreen";  // Show dashboard if logged in
        }

        return "passwordscreen";
    }

    @PostMapping("/home")
    public String showHomeAfterLogin(@RequestParam("username") String username,
                                     @RequestParam("password") String password,
                                     Model model, HttpSession session) {
        UserEntity user = userRepository.findByUsernameAndPassword(username, password);
        if (user != null) {
            // Check if this is OAuth flow
            Boolean isOAuthAttempt = (Boolean) session.getAttribute("oauth_login_attempt");

            if (isOAuthAttempt != null && isOAuthAttempt) {
                // OAuth flow - generate code and redirect
                String clientId = (String) session.getAttribute("oauth_client_id");
                String redirectUri = (String) session.getAttribute("oauth_redirect_uri");
                String scope = (String) session.getAttribute("oauth_scope");


                String code = oAuthService.saveCode(clientId, scope, redirectUri,username);

                // Clear OAuth session
                session.removeAttribute("oauth_client_id");
                session.removeAttribute("oauth_redirect_uri");
                session.removeAttribute("oauth_scope");
                session.removeAttribute("oauth_login_attempt");

                return "redirect:" + redirectUri + "?code=" + code;
            }
            model.addAttribute("username", user.getUsername());
            return "Dashboardscreen";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "passwordscreen";
        }
    }

    @PostMapping("/addUser")
    public String showAddUser(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model) {

        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            model.addAttribute("username", username);
            UserEntity newUser = new UserEntity();
            userRepository.save(newUser);
            return "Dashboardscreen";
        } else {
            model.addAttribute("error", "User already exists");
            return "passwordscreen";
        }
    }
}
