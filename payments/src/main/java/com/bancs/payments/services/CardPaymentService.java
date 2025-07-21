package com.bancs.payments.services;

import com.bancs.payments.data.CardNetworkClient;
import com.bancs.payments.dto.CardNetworkResponse;
import com.bancs.payments.dto.CardPaymentResponse;
import com.bancs.payments.dto.PaymentRequestDTO;
import com.bancs.payments.entity.CardDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CardPaymentService {

    private final EncryptionUtil tokenService;
    private final CardNetworkClient cardNetworkClient;
    private final LedgerService ledgerService;
//    private final ValidationService validationService;
    private static final String PAYLOAD= "payload";


    public CardPaymentResponse handleCardTransaction(PaymentRequestDTO request) {
        String payload = request.getToken();

        if (payload == null || payload.isEmpty()) {
//            return ResponseEntity.badRequest().body("Missing encrypted payload");
            return new CardPaymentResponse("FAILED", "card Details not Found");
        }
        // Step 1: Decrypt/Tokenize
        SecretKey secretKey = EncryptionUtil.getStaticKey();
        String decryptedData = EncryptionUtil.decrypt(payload, secretKey);

//        CardDetails card = tokenService.decryptToken(request.getToken());

        // Step 2: Validate Inputs will be implemented later
//        validationService.validate(card, request.getAmount(), request.getCurrency());

        // Step 3: Authorize with Card Network (e.g., VISA)
        CardDetails card = new CardDetails();
        CardNetworkResponse networkResponse = cardNetworkClient.authorizeTransaction(card, request);

        if (networkResponse.isApproved()) {
            // Step 4: Save to Ledger
            ledgerService.recordTransaction(request, networkResponse);

            return new CardPaymentResponse("SUCCESS", networkResponse.getAuthCode());
        } else {
            return new CardPaymentResponse("DECLINED", networkResponse.getDeclineReason());
        }
    }
}

