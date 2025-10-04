package com.bancs.payments.dto;

import lombok.Data;

@Data
public class PayURequest {
    private String orderId;
    private String amount;
    private String firstname;
    private String email;
    private String phone;

    // getters and setters
}

