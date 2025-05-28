package com.bancs.payments.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private String transactionId;
    private String customerId;
    private String accountNumber;
    private String transactionType;
    private String transactionMode;
    private BigDecimal transactionAmount;
    private String currency;
    private BigDecimal transactionFee;
    private BigDecimal gstAmount;
    private BigDecimal netAmount;
    private String transactionStatus;
    private LocalDateTime transactionDatetime;
    private String merchantName;
    private String channel;
    private Boolean fraudFlag;
}
