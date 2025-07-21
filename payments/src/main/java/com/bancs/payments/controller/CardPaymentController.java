package com.bancs.payments.controller;


import com.bancs.payments.dto.CardPaymentResponse;
import com.bancs.payments.dto.PaymentRequestDTO;
import com.bancs.payments.services.CardPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/card")
public class CardPaymentController {
    @Autowired
    private CardPaymentService cardpaymentService;


    @PostMapping("/payment")
    public ResponseEntity<Map<String,String>> processPayment(@RequestBody PaymentRequestDTO requestDTO){
        CardPaymentResponse response = cardpaymentService.handleCardTransaction(requestDTO);
        String payload = response.toString();
        return ResponseEntity.ok(Map.of("payload",payload));
    }

}
