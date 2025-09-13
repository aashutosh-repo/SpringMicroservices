package com.bancs.payments.dto;

import lombok.Data;

@Data
public class PaytmCallbackDTO {
    private String orderId;
    private String mId;
    private String txnId;
    private String txnAmount;
    private String status;
    private String responseCode;
    private String respMsg;
    private String paymentMode;
    private String txnDate;

    public static PaytmCallbackDTO patytmTestData(){
        PaytmCallbackDTO dto = new PaytmCallbackDTO();
        dto.setOrderId("ORDER12345");
        dto.setMId("MID67890");
        dto.setTxnId("TXN54321");
        dto.setTxnAmount("100.00");
        dto.setStatus("TXN_SUCCESS");
        dto.setResponseCode("01");
        dto.setRespMsg("Transaction Successful");
        dto.setPaymentMode("UPI");
        dto.setTxnDate("2025-10-01 12:34:56");
        return dto;
    }
}
