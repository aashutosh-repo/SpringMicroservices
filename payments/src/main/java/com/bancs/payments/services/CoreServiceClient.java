package com.bancs.payments.services;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CoreServiceClient {
    private final WebClient webClient;

    @Value("${core.service.url}")
    private String coreServiceUrl;

    public String encrypt(String plainText) {
        return webClient.post()
                .uri( "/core/encrypt")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("payload", plainText))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String callDecryptService(String encryptedPayload) {
        return webClient.post()
                .uri("/core/decrypt")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("payload", encryptedPayload))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}