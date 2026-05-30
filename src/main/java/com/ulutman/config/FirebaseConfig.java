package com.ulutman.config;


import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    void init() {
        System.out.println("Firebase disabled");
    }
}
