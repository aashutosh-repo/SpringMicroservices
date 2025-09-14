package com.bancs.payments.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class GatewayResponse {
    private String gatewayName; // Razorpay, PhonePe, ApplePay, etc.
    private String gatewayTxnId; // Razorpay Payment ID / UPI Txn ID
    private String gatewayOrderId; // order id used in Razorpay etc.
    private String gatewayStatus; // Success, Failed, Pending
    private String rawResponse; // store full JSON from gateway (optional)
    private String settlementId; // if gateway provides settlement batch ref
    private String paymentLink; // if payment link is used
    private String failureReason; // if failed, store reason
    private String authCode; // if applicable
    private String settlementDate;

    public static GatewayResponse mockResponse() {
        GatewayResponse response = new GatewayResponse();
        response.setGatewayName("Razorpay");
        response.setGatewayTxnId("txn_123456789");
        response.setGatewayOrderId("order_987654321");
        response.setGatewayStatus("INITIALIZED");
        response.setRawResponse("{\"id\":\"txn_123456789\",\"entity\":\"payment\",\"amount\":5000,\"currency\":\"INR\",\"status\":\"captured\"}");
        response.setSettlementId("settle_12345");
        response.setPaymentLink("https://payment.link/xyz");
        response.setFailureReason(null);
        response.setAuthCode("auth_67890");
        response.setSettlementDate("2024-06-15");
        return response;
    }
}
