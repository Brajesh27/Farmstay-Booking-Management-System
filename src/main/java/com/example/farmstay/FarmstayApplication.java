package com.example.farmstay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FarmstayApplication {
    public static void main(String[] args) {
        SpringApplication.run(FarmstayApplication.class,args);
        System.out.println("Application Running");
    }
}