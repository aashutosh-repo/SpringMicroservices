package com.bancs.payments.services;

import com.bancs.payments.data.CardNetworkClient;
import com.bancs.payments.dto.CardNetworkResponse;
import com.bancs.payments.dto.CardPaymentResponse;
import com.bancs.payments.dto.PaymentRequestDTO;
import com.bancs.payments.entity.CardDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardPaymentService {

    private final EncryptionUtil tokenService;
    private final CardNetworkClient cardNetworkClient;
    private final LedgerService ledgerService;
    private final CoreServiceClient coreServiceClient;
//    private final ValidationService validationService;
    private static final String PAYLOAD= "payload";


    public CardPaymentResponse handleCardTransaction(PaymentRequestDTO request) {
        String payload = request.getToken();

        if (payload == null || payload.isEmpty()) {
            return new CardPaymentResponse("FAILED", "card Details not Found");
        }
        // Step 1: Decrypt/Tokenize
        String decryptedData = coreServiceClient.callDecryptService(payload);

        // Step 2: Inputs Validation will be implemented later
//        validationService.validate(card, request.getAmount(), request.getCurrency());

        // Step 3: Authorize with Card Network (e.g., VISA)
        CardDetails card = new CardDetails();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
             card = objectMapper.readValue(decryptedData, CardDetails.class);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
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

