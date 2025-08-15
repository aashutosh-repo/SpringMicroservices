package com.bancs.payments.dto;

import com.bancs.payments.utility.BillingAddress;
import com.bancs.payments.utility.PaymentRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class UpiPaymentRequest extends PaymentRequest {
    private String upiId;
}
