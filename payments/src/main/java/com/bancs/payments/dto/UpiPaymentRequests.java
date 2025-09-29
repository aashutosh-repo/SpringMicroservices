package com.bancs.payments.dto;

import lombok.Data;

@Data
public class UpiPaymentRequests {
    private String txnId;
    private String amount;
    private String productInfo;
    private String firstName;
    private String email;
    private String phone;
    private String vpa;
}
