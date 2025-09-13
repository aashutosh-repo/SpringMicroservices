package com.bancs.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private String transactionId;   // your internal txn ID
    private String gateway;         // PHONEPE / PAYTM / RAZORPAY
    private String status;          // SUCCESS / FAILURE / PENDING
    private String amount;          // "500.00"
    private String method;          // UPI / CARD / WALLET
    private String referenceId;     // gateway txn ref (UTR, TXNID, etc.)
    private String message;
    private String paidAt;

    public static PaymentResponse failureResponse(String message) {
        return new PaymentResponse(null, null, "FAILURE", null, null, null, message, null);
    }
    public static PaymentResponse pendingResponse(String message) {
        return new PaymentResponse(null, null, "PENDING", null, null, null, message, null);
    }
    public static PaymentResponse successResponse() {
        return new PaymentResponse("TXN1234", "PHONEPE", "SUCCESS", "1000", "UPI", "REF1919199", "payment Success", "2023-10-10T10:10:10" );
    }
}
