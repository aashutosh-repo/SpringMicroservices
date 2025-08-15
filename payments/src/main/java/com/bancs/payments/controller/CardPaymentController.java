package com.bancs.payments.controller;


import com.bancs.payments.dto.PaymentResponse;
import com.bancs.payments.dto.PaymentRequestDTO;
import com.bancs.payments.services.CardPaymentService;
import com.bancs.payments.services.CoreServiceClient;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/card")
@AllArgsConstructor
public class CardPaymentController {
    private final CardPaymentService cardpaymentService;
    private final CoreServiceClient serviceClient;


    @PostMapping("/payment")
    public ResponseEntity<Map<String,String>> processPayment(@RequestBody PaymentRequestDTO requestDTO){
        PaymentResponse response = cardpaymentService.handleCardTransaction(requestDTO);
        String payload = response.toString();
        return ResponseEntity.ok(Map.of("payload",payload));
    }
    @PostMapping("/encrypt")
    public ResponseEntity<String> encrypt(@RequestBody String str){
        String str1= serviceClient.encrypt(str);
        return ResponseEntity.ok(str1);
    }

    @PostMapping("/decrypt")
    public ResponseEntity<String> decrypt(@RequestBody String str){
        String str1= serviceClient.callDecryptService(str);
        return ResponseEntity.ok(str1);
    }

}
