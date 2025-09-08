package com.microservices.controllers;

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
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String showPasswordPage() {
        return "passwordscreen";
    }

    @PostMapping("/home")
    public String showHome(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model) {

        UserEntity user = userRepository.findByUsernameAndPassword(username, password);

        if (user != null) {
            model.addAttribute("username", user.getUsername());
            return "Dashboardscreen";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "passwordscreen";
        }
    }

    @PostMapping("/addUser")
    public String showUser(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           Model model) {

        UserEntity user = userRepository.findByUsername(username);

        if (user == null) {
            model.addAttribute("username", username);
            UserEntity newUser = new UserEntity();
            newUser.setUsername(username);
            newUser.setPassword(password);
            userRepository.save(newUser);
            return "Dashboardscreen";
        } else {
            model.addAttribute("error", "User already exists");
            return "passwordscreen";
        }
    }
}
