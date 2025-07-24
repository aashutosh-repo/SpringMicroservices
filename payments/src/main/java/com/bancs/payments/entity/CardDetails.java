package com.bancs.payments.entity;

import lombok.Data;

@Data
public class CardDetails {
    private String number;
    private String expiry;
    private String cvv;
}
