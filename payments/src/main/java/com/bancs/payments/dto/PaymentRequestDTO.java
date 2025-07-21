package com.bancs.payments.dto;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private String token;
    private Double amount;
    private String currency;
    private String merchantId;
    private String customerId;
    private String txnType;
}
