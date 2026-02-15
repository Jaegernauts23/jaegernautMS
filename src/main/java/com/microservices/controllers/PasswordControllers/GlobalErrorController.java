package com.microservices.controllers.PasswordControllers;

import org.springframework.context.annotation.Conditional;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalErrorController {

    @ExceptionHandler(Exception.class)
    public String handleException(){
        return "error";
    }
}
