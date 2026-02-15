package com.microservices.controllers.HomeDashboardControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HRDPageDisplayController {
    @GetMapping("/")
    public String showDisplayPage(){
        return "HomeDashboardPage/HRDashboardPage";
    }
}
