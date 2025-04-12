package com.bancs.payments.controller;

import com.bancs.payments.services.CoreServiceClient;
import com.bancs.payments.services.EncryptionUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {

    private final WebClient webClient;
    private final CoreServiceClient coreServiceClient;


    @PostMapping("/process-payment")
    public ResponseEntity<String> processPayment(@RequestBody Map<String, String> requestData){

        String encryptedPayload = requestData.get("encryptedPayload");

//        Mono<ResponseEntity<String>> string = coreServiceClient.getDecryptedData(encryptedPayload)
//                .map(decryptedData -> {
//                    System.out.println("Decrypted Payment Data: " + decryptedData);
//                    return ResponseEntity.ok("Payment Processed with Data: " + decryptedData);
//                })
//                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body("Decryption failed: " + e.getMessage())));
//        System.out.println(string);
        return ResponseEntity.ok("Transaction Successful");
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

    @PostMapping("/testPayment")
    public ResponseEntity<Map<String,String>> processPaymentTest(@RequestBody Map<String, String> requestData) {
        try {
            // Extract the encrypted payload from the request data
            String encryptedPayload = requestData.get("payload");

            if (encryptedPayload == null || encryptedPayload.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("payload","Missing encrypted payload"));
            }
            SecretKey secretKey = EncryptionUtil.getStaticKey();

            // Decrypt the payload using the decryption utility
            String decryptedData = EncryptionUtil.decrypt(encryptedPayload, secretKey);

            // Log decrypted data (or process it as per your requirement)
            System.out.println("Decrypted Payment Data: " + decryptedData);

            // Here you can parse the decrypted data and process the payment logic
            // For now, we'll just simulate successful transaction processing

            return ResponseEntity.ok(Map.of("payload", "Transaction Successful"));
        } catch (Exception e) {
            // Handle decryption or other errors
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("payload","Error processing payment"));
        }
    }


}
