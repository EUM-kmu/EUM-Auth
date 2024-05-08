package com.eum.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000","https://hanmaeul.vercel.app/","https://k-eum2023.web.app")
                .allowedMethods("GET", "POST", "PUT", "DELETE").exposedHeaders("Custom-Header")
                .allowCredentials(true)
                .maxAge(3600);
    }}
