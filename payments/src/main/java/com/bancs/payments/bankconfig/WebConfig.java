package com.bancs.payments.bankconfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig{
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Register the Java 8 date/time module
        return objectMapper;
    }
//
    @Bean
    public WebClient webClient() {
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }
}
