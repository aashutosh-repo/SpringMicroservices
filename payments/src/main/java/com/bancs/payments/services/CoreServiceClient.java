package com.bancs.payments.services;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.Collections;

@Service
@AllArgsConstructor
public class CoreServiceClient {

    private final WebClient webClient;

    public CoreServiceClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8084/core").build(); // Replace with actual URL
    }

    public Mono<String> getDecryptedData(String encryptedPayload) {
        return webClient.post()
                .uri("/secureCard/tokenize")
                .bodyValue(Collections.singletonMap("encryptedPayload", encryptedPayload))
                .retrieve()
                .bodyToMono(String.class);
    }
}