package com.bancs.payments.services;

import com.bancs.payments.dto.CardNetworkResponse;
import com.bancs.payments.dto.PaymentRequestDTO;
import com.bancs.payments.entity.Transaction;
import com.bancs.payments.repository.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LedgerService {
    private PaymentsRepository txnRepo;

    public void recordTransaction(PaymentRequestDTO req, CardNetworkResponse netResp) {
        Transaction txn = new Transaction();
        txn.setCustomerId(req.getCustomerId());
        txn.setMerchantId(req.getMerchantId());
        txn.setTransactionAmount(BigDecimal.valueOf(req.getAmount()));
//        txn.setAuthCode(netResp.getAuthCode());
        txn.setTransactionStatus("SUCCESS");
        txn.setTransactionDatetime(LocalDateTime.now());

        txnRepo.save(txn);
    }
}

