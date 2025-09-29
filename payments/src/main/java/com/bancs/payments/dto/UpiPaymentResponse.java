package com.bancs.payments.dto;

import lombok.Data;

@Data
public class UpiPaymentResponse {
    private String status;
    private String message;
    private Object gatewayResponse;
}
