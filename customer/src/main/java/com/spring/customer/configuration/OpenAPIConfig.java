package com.spring.customer.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .servers(List.of(
                        new Server().url("http://localhost:8082").description("Local Server"),
                        new Server().url("https://staging.example.com").description("Staging Server"),
                        new Server().url("https://api.example.com").description("Production Server")
                ));
    }
}
