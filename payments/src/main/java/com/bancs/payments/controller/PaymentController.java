package com.bancs.payments.controller;

import com.bancs.payments.services.CoreServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {

    private final WebClient webClient;
    private final CoreServiceClient coreServiceClient;


    @PostMapping("/process-payment")
    public Mono<ResponseEntity<String>> processPayment(@RequestBody Map<String, String> requestData){

        String encryptedPayload = requestData.get("encryptedPayload");

        Mono<ResponseEntity<String>> string = coreServiceClient.getDecryptedData(encryptedPayload)
                .map(decryptedData -> {
                    System.out.println("Decrypted Payment Data: " + decryptedData);
                    return ResponseEntity.ok("Payment Processed with Data: " + decryptedData);
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Decryption failed: " + e.getMessage())));
        System.out.println(string);
        return string;
    }



//        String deToken= webClient.get()
//                .uri(
//        uriBuilder -> uriBuilder.path("api/token/detokenize")
//                .queryParam("token", request.get("token"))
//                .build())
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//        System.out.println("Your Card Number is : "+deToken);

//        return ResponseEntity.ok("payment Successful");
//    }



}
