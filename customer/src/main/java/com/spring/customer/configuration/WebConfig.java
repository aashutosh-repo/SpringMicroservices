package com.spring.customer.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Allow all endpoints
//                .allowedOrigins("http://localhost:8080") // Allow requests from the gateway
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific methods
//                .allowedHeaders("*") // Allow all headers
//                .allowCredentials(true); // Allow credentials if needed
//    }
//}