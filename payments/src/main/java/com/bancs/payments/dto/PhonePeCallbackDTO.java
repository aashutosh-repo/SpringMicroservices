package com.bancs.payments.dto;

import lombok.Data;

@Data
public class PhonePeCallbackDTO {
    private String merchantId;
    private String transactionId;
    private Long amount;
    private String status;
    private String providerReferenceId;
    private PaymentInstrument paymentInstrument;

    @Data
    public static class PaymentInstrument {
        private String type;
        private String utr;
    }
    public static PhonePeCallbackDTO phonePeTestData() {
        PhonePeCallbackDTO dto = new PhonePeCallbackDTO();
        dto.setMerchantId("MERCHANT12345");
        dto.setTransactionId("TXN67890");
        dto.setAmount(10000L); // Amount in paise
        dto.setStatus("SUCCESS");
        dto.setProviderReferenceId("PROVREF54321");
        PaymentInstrument pi = new PaymentInstrument();
        pi.setType("UPI");
        pi.setUtr("UTR123456789");
        dto.setPaymentInstrument(pi);
        return dto;
    }
}
