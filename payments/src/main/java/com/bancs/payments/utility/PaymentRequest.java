package com.bancs.payments.utility;


import com.bancs.payments.dto.CardPaymentRequest;
import com.bancs.payments.dto.NetBankingPaymentRequest;
import com.bancs.payments.dto.UpiPaymentRequest;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "paymentMethod",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UpiPaymentRequest.class, name = "UPI"),
        @JsonSubTypes.Type(value = CardPaymentRequest.class, name = "CARD"),
        @JsonSubTypes.Type(value = NetBankingPaymentRequest.class, name = "NET_BANKING")
})
@Data
@RequiredArgsConstructor
public abstract class PaymentRequest {
    private String paymentMethod;
    private String token;
    private String merchantId;
    private String customerId;
    private BigDecimal amount;
    private String currency;
    private BillingAddress billingAddress;
}
