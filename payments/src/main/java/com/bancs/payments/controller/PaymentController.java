package com.bancs.payments.controller;

import com.bancs.payments.dto.PaymentSummary;
import com.bancs.payments.dto.TransactionDTO;
import com.bancs.payments.services.CoreServiceClient;
import com.bancs.payments.services.EncryptionUtil;
import com.bancs.payments.services.PaymentService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/payments")
@AllArgsConstructor
public class PaymentController {

    private static final Logger log = LogManager.getLogger(PaymentController.class);
    private static final String PAYLOAD= "payload";
    private final CoreServiceClient coreServiceClient;
    private final PaymentService paymentService;

    @PostMapping("/process-payment")
    public ResponseEntity<Map<String,String>> processPayment(@RequestBody Map<String, String> requestData){
        try {
            String encryptedPayload = requestData.get(PAYLOAD);
            if (encryptedPayload == null || encryptedPayload.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(PAYLOAD,"Missing encrypted payload"));
            }
            SecretKey secretKey = EncryptionUtil.getStaticKey();
            log.info("Decryption in progress...");
            String decrpt = coreServiceClient.callDecryptService(encryptedPayload);
            String decryptedData = EncryptionUtil.decrypt(encryptedPayload);
              log.info("Decryption completed...");
            log.info("The Decrypted data : {}",decrpt);
            log.info("The Decrypted data : {}",decryptedData);
            // Further parse the decrypted data and process the payment logic
            String resMessage = "Transaction Successful";
            String jsonResponse = String.format("{\"payload\": \"%s\"}", resMessage);
            String encryptData = coreServiceClient.encrypt(resMessage);
            return ResponseEntity.ok(Map.of(PAYLOAD, encryptData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(PAYLOAD,"Error processing payment"));
        }
    }

    @PostMapping("/testPayment")
    public ResponseEntity<Map<String,String>> processPaymentTest(@RequestBody Map<String, String> requestData) {
        try {
            String encryptedPayload = requestData.get(PAYLOAD);
            if (encryptedPayload == null || encryptedPayload.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(PAYLOAD,"Missing encrypted payload"));
            }
            SecretKey secretKey = EncryptionUtil.getStaticKey();
            String decryptedData = EncryptionUtil.decrypt(encryptedPayload);
            // Further parse the decrypted data and process the payment logic
            String resMessage = "Transaction Successful";
            String encryptData = EncryptionUtil.encrypt(resMessage,secretKey);
            return ResponseEntity.ok(Map.of(PAYLOAD, encryptData));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(PAYLOAD,"Error processing payment"));
        }
    }

    @GetMapping("/getPaymentDetails")
    public ResponseEntity<List<TransactionDTO>> getTransactionDetails(){
        List<TransactionDTO> transactionDTOList =  paymentService.getTransactionDetails();
        return ResponseEntity.ok(transactionDTOList);
    }

    @GetMapping("/initiate")
    public Mono<PaymentSummary> initiatePayment(
            @RequestParam String customerId,
            @RequestParam String accountId
    ) {
        return paymentService.initiatePayment(customerId, accountId);
    }


}
