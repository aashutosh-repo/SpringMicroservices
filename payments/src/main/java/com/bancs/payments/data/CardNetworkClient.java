package com.bancs.payments.data;

import com.bancs.payments.dto.CardNetworkResponse;
import com.bancs.payments.dto.PaymentRequestDTO;
import com.bancs.payments.entity.CardDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CardNetworkClient {

    public CardNetworkResponse authorizeTransaction(CardDetails card, PaymentRequestDTO req) {
        // Simulate a REST call to VISA/MasterCard API or internal ISO 8583 handler

        // Build request payload
        Map<String, Object> payload = Map.of(
                "cardNumber", card.getNumber(),
                "expiry", card.getExpiry(),
                "cvv", card.getCvv(),
                "amount", req.getAmount(),
                "currency", req.getCurrency(),
                "merchantId", req.getMerchantId()
        );

        // Normally: use WebClient or RestTemplate to hit VISA/MasterCard endpoints
        // Here: Mock response
        return new CardNetworkResponse(true, "AUTH123456", null);
    }
}
