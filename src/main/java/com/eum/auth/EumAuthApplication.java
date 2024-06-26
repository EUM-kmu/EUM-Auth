package com.eum.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
@EnableJpaAuditing
public class EumAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(EumAuthApplication.class, args);
    }


}