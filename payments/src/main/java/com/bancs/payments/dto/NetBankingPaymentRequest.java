package com.bancs.payments.dto;

import com.bancs.payments.utility.PaymentRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class NetBankingPaymentRequest extends PaymentRequest {
    private String bankCode;
    private String username;
    private String transactionPassword;
}
