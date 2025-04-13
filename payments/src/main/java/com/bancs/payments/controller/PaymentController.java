package com.bancs.payments.controller;

import com.bancs.payments.services.CoreServiceClient;
import com.bancs.payments.services.EncryptionUtil;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

    private static final Logger log = LogManager.getLogger(PaymentController.class);
    private static final String PAYLOAD= "payload";
    private final WebClient webClient;
    private final CoreServiceClient coreServiceClient;


    @PostMapping("/process-payment")
    public ResponseEntity<Map<String,String>> processPayment(@RequestBody Map<String, String> requestData){
        try {
            String encryptedPayload = requestData.get(PAYLOAD);
            if (encryptedPayload == null || encryptedPayload.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(PAYLOAD,"Missing encrypted payload"));
            }
            SecretKey secretKey = EncryptionUtil.getStaticKey();
            log.info("Decryption in progress...");
            String decryptedData = EncryptionUtil.decrypt(encryptedPayload, secretKey);
            log.info("Decryption completed...");
            log.info("The Decrypted data : {}",decryptedData);
            // Further parse the decrypted data and process the payment logic
            String resMessage = "Transaction Successful";
            String encryptData = EncryptionUtil.encrypt(resMessage,secretKey);
            return ResponseEntity.ok(Map.of(PAYLOAD, encryptData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(PAYLOAD,"Error processing payment"));
        }
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
            String encryptedPayload = requestData.get(PAYLOAD);
            if (encryptedPayload == null || encryptedPayload.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(PAYLOAD,"Missing encrypted payload"));
            }
            SecretKey secretKey = EncryptionUtil.getStaticKey();
            String decryptedData = EncryptionUtil.decrypt(encryptedPayload, secretKey);
            // Further parse the decrypted data and process the payment logic
            String resMessage = "Transaction Successful";
            String encryptData = EncryptionUtil.encrypt(resMessage,secretKey);
            return ResponseEntity.ok(Map.of(PAYLOAD, encryptData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(PAYLOAD,"Error processing payment"));
        }
    }


}
