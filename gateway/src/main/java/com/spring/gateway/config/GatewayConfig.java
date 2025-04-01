package com.spring.gateway.config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()

                // 🔹 Route for Security Service (Forward `/auth/security/**` to `8081`)
                .route("security-service", r -> r.path("/auth/security/**")
                        .uri("http://localhost:8081"))
                .route("customer-service", r -> r.path("/customer/**")
                        .uri("http://localhost:8082"))
                .route("account-service", r -> r.path("/account/**")
                        .uri("http://localhost:8083"))
                .route("core-service", r -> r.path("/core/**")
                        .uri("http://localhost:8084"))
                .route("swagger-ui", r -> r.path("/swagger-ui/**", "/v3/api-docs/**", "/v3/api-docs/swagger-config")
                        .uri("http://localhost:8081"))
                .build();
    }
}
