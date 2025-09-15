package com.bancs.payments.controller;

import com.bancs.payments.dto.*;
import com.bancs.payments.services.CoreServiceClient;
import com.bancs.payments.services.EncryptionUtil;
import com.bancs.payments.services.PaymentService;
import com.bancs.payments.services.TransactionService;
import com.bancs.payments.utility.PaymentRequest;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
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
    private TransactionService transactionService;

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

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponse> initiatePayment(@RequestBody PaymentRequest request) {
        return switch (request.getPaymentMethod()) {
            case "UPI" -> {
                UpiPaymentRequest upiPaymentRequest = (UpiPaymentRequest) request;
                TransactionDTO transactionDTO= new TransactionDTO();
                transactionDTO.setTransactionAmount(upiPaymentRequest.getAmount());
                transactionDTO.setCurrency(upiPaymentRequest.getCurrency());
                transactionDTO.setCustomerId(upiPaymentRequest.getCustomerId());
                transactionDTO.setMerchantName(upiPaymentRequest.getMerchantId());
                transactionDTO.setTransactionType(upiPaymentRequest.getPaymentMethod());
                transactionDTO.setOrderId(upiPaymentRequest.getOrderId());
                PaymentInitResponse paymentInitResponse= transactionService.initiateTransaction(transactionDTO);
                yield ResponseEntity.ok(PaymentResponse.successResponse());
                //upi payment Logic here
            }
            case "CARD" -> {
                CardPaymentRequest cardPaymentRequest = (CardPaymentRequest) request;
                yield ResponseEntity.ok(PaymentResponse.successResponse());
                //Handle CARD payment Request
            }
            case "NET_BANKING" -> {
                NetBankingPaymentRequest netBankingPaymentRequest = (NetBankingPaymentRequest) request;
                yield ResponseEntity.ok(PaymentResponse.successResponse());
            }
            default -> ResponseEntity.ok(PaymentResponse.failureResponse("Unsupported payment method"));
        };
    }

    @PostMapping("/callback/phonepe")
    public ResponseEntity<PaymentResponse> handlePhonePeCallback(@RequestBody PhonePeCallbackDTO dto) {
        PaymentResponse response = new PaymentResponse(
                dto.getTransactionId(),
                "PHONEPE",
                dto.getStatus().equals("SUCCESS") ? "SUCCESS" : "FAILURE",
                String.valueOf(dto.getAmount() / 100.0),   // convert paise to rupees
                dto.getPaymentInstrument().getType(),
                dto.getProviderReferenceId(),
                "Payment via PhonePe",
                LocalDateTime.now().toString()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/callback/paytm")
    public ResponseEntity<PaymentResponse> handlePaytmCallback(@RequestBody PaytmCallbackDTO dto) {
        PaymentResponse response = new PaymentResponse(
                dto.getOrderId(),
                "PAYTM",
                dto.getStatus().equals("TXN_SUCCESS") ? "SUCCESS" : "FAILURE",
                dto.getTxnAmount(),
                dto.getPaymentMode(),
                dto.getTxnId(),
                dto.getMId(),
                dto.getTxnDate()
        );

        paymentService.updatePaymentStatus(response);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{txnId}/status")
    public ResponseEntity<PaymentResponse> getStatus(@PathVariable String txnId) {
        PaymentResponse response = transactionService.getTransactionStatus(txnId);
        return ResponseEntity.ok(response);
    }

}
