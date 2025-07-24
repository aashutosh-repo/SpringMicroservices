package com.bancs.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CardPaymentResponse {
    private String status;
    private String message;
}
