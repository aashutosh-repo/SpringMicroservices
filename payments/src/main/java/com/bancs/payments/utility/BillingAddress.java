package com.bancs.payments.utility;

import lombok.Data;

@Data
public class BillingAddress {
    private String payerName;
    private String city;
    private String fullAddress;
    private String country;
    private String pincode;
}