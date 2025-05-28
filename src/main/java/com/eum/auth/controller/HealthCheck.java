package com.eum.auth.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping
@RestController
public class HealthCheck {
    @Hidden
    @GetMapping
    public String check(){
        return "ok";
    }
    @Hidden
    @GetMapping("favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
    }
}
