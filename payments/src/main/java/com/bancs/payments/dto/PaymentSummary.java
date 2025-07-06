package com.bancs.payments.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PaymentSummary {
    private String customerName;
    private String accountBalance;

    public PaymentSummary(CustomerDto customer, AccountDto account) {
        this.customerName = customer.getFirstName();
        this.accountBalance = String.valueOf(account.getAvailable_balance());
    }
}
