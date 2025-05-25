package com.spring.batch.reader;

import com.spring.batch.configuration.InputFileProperties;
import com.spring.batch.demoentity.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class TransactionReader {
    private final InputFileProperties inputFileProperties;
    private final GenericFlatFileReaderFactory factory;

    @Bean
    public FlatFileItemReader<Transaction> transactionsReader() {
        return factory.buildReader(
                inputFileProperties.getPath(),
                Transaction.class,
                        new String[]{
                        "transactionId", "customerId", "accountNumber", "transactionType", "transactionMode", "transactionCategory",
                        "transactionAmount", "currency", "transactionStatus", "transactionDatetime", "valueDate", "merchantId",
                        "merchantName", "merchantCategoryCode", "location", "geoLatitude", "geoLongitude", "deviceId", "deviceType",
                        "ipAddress", "browserInfo", "channel", "bankBranchCode", "initiatedBy", "referenceNumber", "authorizationCode",
                        "reversalFlag", "parentTransactionId", "remarks", "transactionFee", "gstAmount", "netAmount", "fraudFlag",
                        "riskScore", "createdAt", "updatedAt", "createdBy", "updatedBy"
                },1);
    }

    @Bean
    public FieldSetMapper<Transaction> customTransactionFieldSetMapper() {
        return fieldSet -> {
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

            return Transaction.builder()
                    .transactionId(fieldSet.readString("transactionId"))
                    .customerId(fieldSet.readString("customerId"))
                    .accountNumber(fieldSet.readString("accountNumber"))
                    .transactionType(fieldSet.readString("transactionType"))
                    .transactionMode(fieldSet.readString("transactionMode"))
                    .transactionCategory(fieldSet.readString("transactionCategory"))
                    .transactionAmount(fieldSet.readBigDecimal("transactionAmount"))
                    .currency(fieldSet.readString("currency"))
                    .transactionStatus(fieldSet.readString("transactionStatus"))
                    .transactionDatetime(LocalDateTime.parse(fieldSet.readString("transactionDatetime"), formatter))
                    .valueDate(LocalDateTime.parse(fieldSet.readString("valueDate"), formatter))
                    .merchantId(fieldSet.readString("merchantId"))
                    .merchantName(fieldSet.readString("merchantName"))
                    .merchantCategoryCode(fieldSet.readString("merchantCategoryCode"))
                    .location(fieldSet.readString("location"))
                    .geoLatitude(fieldSet.readDouble("geoLatitude"))
                    .geoLongitude(fieldSet.readDouble("geoLongitude"))
                    .deviceId(fieldSet.readString("deviceId"))
                    .deviceType(fieldSet.readString("deviceType"))
                    .ipAddress(fieldSet.readString("ipAddress"))
                    .browserInfo(fieldSet.readString("browserInfo"))
                    .channel(fieldSet.readString("channel"))
                    .bankBranchCode(fieldSet.readString("bankBranchCode"))
                    .initiatedBy(fieldSet.readString("initiatedBy"))
                    .referenceNumber(fieldSet.readString("referenceNumber"))
                    .authorizationCode(fieldSet.readString("authorizationCode"))
                    .reversalFlag(Boolean.parseBoolean(fieldSet.readString("reversalFlag")))
                    .parentTransactionId(fieldSet.readString("parentTransactionId"))
                    .remarks(fieldSet.readString("remarks"))
                    .transactionFee(fieldSet.readBigDecimal("transactionFee"))
                    .gstAmount(fieldSet.readBigDecimal("gstAmount"))
                    .netAmount(fieldSet.readBigDecimal("netAmount"))
                    .fraudFlag(Boolean.parseBoolean(fieldSet.readString("fraudFlag")))
                    .riskScore(fieldSet.readInt("riskScore"))
                    .createdAt(LocalDateTime.parse(fieldSet.readString("createdAt"), formatter))
                    .updatedAt(LocalDateTime.parse(fieldSet.readString("updatedAt"), formatter))
                    .createdBy(fieldSet.readString("createdBy"))
                    .updatedBy(fieldSet.readString("updatedBy"))
                    .build();
        };
    }

}
