package com.bancs.payments.mapper;

import com.bancs.payments.dto.TransactionDTO;
import com.bancs.payments.entity.Transaction;

public class TransactionMapper {
    private TransactionMapper(){}

    public static TransactionDTO toDTO(Transaction txn) {
        TransactionDTO dto = new TransactionDTO();
        dto.setTransactionId(txn.getTransactionId());
        dto.setOrderId(txn.getExtRefId());
        dto.setCustomerId(txn.getCustomerId());
        dto.setAccountNumber(txn.getAccountNumber());
        dto.setTransactionType(txn.getTransactionType());
        dto.setTransactionMode(txn.getTransactionMode());
        dto.setTransactionAmount(txn.getTransactionAmount());
        dto.setCurrency(txn.getCurrency());
        dto.setTransactionFee(txn.getTransactionFee());
        dto.setGstAmount(txn.getGstAmount());
        dto.setNetAmount(txn.getNetAmount());
        dto.setTransactionStatus(txn.getTransactionStatus());
        dto.setTransactionDatetime(txn.getTransactionDatetime());
        dto.setMerchantName(txn.getMerchantName());
        dto.setChannel(txn.getChannel());
        dto.setFraudFlag(txn.getFraudFlag());
        return dto;
    }

    public static Transaction toEntity(TransactionDTO dto) {
        if (dto == null) return null;
        Transaction txn = new Transaction();
        txn.setTransactionId(dto.getTransactionId());
        txn.setExtRefId(dto.getOrderId());
        txn.setCustomerId(dto.getCustomerId());
        txn.setAccountNumber(dto.getAccountNumber());
        txn.setTransactionType(dto.getTransactionType());
        txn.setTransactionMode(dto.getTransactionMode());
        txn.setTransactionAmount(dto.getTransactionAmount());
        txn.setCurrency(dto.getCurrency());
        txn.setTransactionFee(dto.getTransactionFee());
        txn.setGstAmount(dto.getGstAmount());
        txn.setNetAmount(dto.getNetAmount());
        txn.setTransactionStatus(dto.getTransactionStatus());
        txn.setTransactionDatetime(dto.getTransactionDatetime());
        txn.setMerchantName(dto.getMerchantName());
        txn.setChannel(dto.getChannel());
        txn.setFraudFlag(dto.getFraudFlag());
        return txn;
    }
}

