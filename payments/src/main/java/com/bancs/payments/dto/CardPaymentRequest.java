package com.bancs.payments.dto;

import com.bancs.payments.utility.BillingAddress;
import com.bancs.payments.utility.PaymentRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class CardPaymentRequest extends PaymentRequest {
    private String cardNumber;
    private String expiryMonth;
    private String expiryYear;
    private String cvv;
    private String cardHolderName;
}
