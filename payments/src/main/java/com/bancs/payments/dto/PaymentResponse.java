package com.bancs.payments.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {
    private String transactionId;   // your internal txn ID
    private String gateway;         // PHONEPE / PAYTM / RAZORPAY
    private String status;          // SUCCESS / FAILURE / PENDING
    private String amount;
    private String currrency;        // INR
    private String method;          // UPI / CARD / WALLET
    private String referenceId;     // gateway txn ref (UTR, TXNID, etc.)
    private String message;
    private String paidAt;
    private String errorCode;


    public static PaymentResponse failureResponse(String message) {
        return new PaymentResponse(null, null, "FAILURE", null, null,null, null, message, null,null);
    }
    public static PaymentResponse pendingResponse(String message) {
        return new PaymentResponse(null, null, "PENDING", null, null,"", null, message, null,null);
    }
    public static PaymentResponse successResponse() {
        return new PaymentResponse("TXN1234", "PHONEPE", "SUCCESS", "1000",null, "UPI", "REF1919199", "payment Success", "2023-10-10T10:10:10",null );
    }
}
