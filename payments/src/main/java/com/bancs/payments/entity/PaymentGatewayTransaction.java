package com.bancs.payments.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentGatewayTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "transaction_id", nullable = false)
    private String transactionId; // FK to your Transaction table

    @Column(name = "gateway_name")
    private String gatewayName; // Razorpay, PhonePe, ApplePay, etc.

    @Column(name = "gateway_txn_id")
    private String gatewayTxnId; // Razorpay Payment ID / UPI Txn ID

    @Column(name = "gateway_order_id")
    private String gatewayOrderId; // order id used in Razorpay etc.

    @Column(name = "gateway_status")
    private String gatewayStatus; // Success, Failed, Pending

    @Column(name = "raw_response", columnDefinition = "TEXT")
    private String rawResponse; // store full JSON from gateway (optional)

    @Column(name = "settlement_id")
    private String settlementId; // if gateway provides settlement batch ref

    @Column(name = "settlement_date")
    private LocalDateTime settlementDate;
}
