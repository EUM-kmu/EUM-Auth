package com.eum.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
public class EumAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(EumAuthApplication.class, args);
    }
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**").allowedOrigins("http://localhost:3000").allowedMethods("GET", "POST", "PUT", "DELETE")
////                        .allowedHeaders("Authorization", "Content-Type")
////                        .exposedHeaders("Custom-Header")
//                        .allowCredentials(true)
//                        .maxAge(3600);;
//            }
//        };
//    }

}