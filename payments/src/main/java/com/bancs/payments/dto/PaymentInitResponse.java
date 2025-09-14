package com.bancs.payments.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInitResponse {
    private String transactionId;
    private String gatewayName;
    private String gatewayOrderId;
    private String paymentLink;
    private BigDecimal amount;
    private String currency;
    private String status;

    public PaymentInitResponse(String paymentLink, String transactionId) {
        this.paymentLink = paymentLink;
        this.transactionId = transactionId;
    }
}

