package com.bancs.payments.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {

    // Primary Key
    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "transaction_id", nullable = false, updatable = false)
    private String transactionId;

    private String extRefId; // External Reference ID from payment gateway or external system

    @Column(name = "txn_seq")
    private Long txnSeq;
    // Basic Transaction Details
    @Column(name = "customer_id", nullable = false)
    private String customerId;

    @Column(name = "account_number", nullable = true)
    private String accountNumber;

    @Column(name = "transaction_type", nullable = false)
    private String transactionType; // credit, debit, refund, reversal

    @Column(name = "transaction_mode")
    private String transactionMode; // Card, UPI, QR, Net Banking, etc.

    @Column(name = "transaction_category")
    private String transactionCategory; // shopping, fuel, transfer, etc.

    @Column(name = "transaction_amount", precision = 18, scale = 2, nullable = false)
    private BigDecimal transactionAmount;

    @Column(name = "cr_dr_flag", length = 2)
    private int crDrFlag; // 1 for Credit, 2 for Debit

    @Column(name = "currency", length = 3)
    private String currency; // e.g., INR, USD

    @Column(name = "transaction_status")
    private String transactionStatus; // Success, Failed, Pending

    @Column(name = "txn_desc")
    private String txnDesc;

    @Column(name = "transaction_datetime")
    private LocalDateTime transactionDatetime;

    @Column(name = "value_date")
    private LocalDateTime valueDate;

    // Merchant Info
    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "merchant_category_code")
    private String merchantCategoryCode; // MCC

    // Origin Info
    @Column(name = "location")
    private String location;

    @Column(name = "geo_latitude")
    private Double geoLatitude;

    @Column(name = "geo_longitude")
    private Double geoLongitude;

    @Column(name = "device_id")
    private String deviceId;

    @Column(name = "device_type")
    private String deviceType;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "browser_info", length = 1000)
    private String browserInfo;

    @Column(name = "channel")
    private String channel; // Web, Mobile, ATM, POS

    @Column(name = "bank_branch_code")
    private String bankBranchCode;

    @Column(name = "initiated_by")
    private String initiatedBy; // customer, bank, merchant

    // Reference Info
    @Column(name = "reference_number", unique = true)
    private String referenceNumber;

    @Column(name = "authorization_code")
    private String authorizationCode;

    @Column(name = "reversal_flag")
    private Boolean reversalFlag;

    @Column(name = "parent_transaction_id")
    private String parentTransactionId;

    @Column(name = "remarks", length = 1000)
    private String remarks;

    // Financial Metadata
    @Column(name = "transaction_fee", precision = 18, scale = 2)
    private BigDecimal transactionFee;

    @Column(name = "gst_amount", precision = 18, scale = 2)
    private BigDecimal gstAmount;

    @Column(name = "net_amount", precision = 18, scale = 2)
    private BigDecimal netAmount;

    // Fraud/Risk Fields
    @Column(name = "fraud_flag")
    private Boolean fraudFlag;

    @Column(name = "risk_score")
    private Integer riskScore;

    // Audit Columns
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    // Lifecycle Hooks
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
