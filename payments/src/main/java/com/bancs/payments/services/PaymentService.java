package com.bancs.payments.services;

import com.bancs.payments.dto.TransactionDTO;
import com.bancs.payments.entity.Transaction;
import com.bancs.payments.mapper.TransactionMapper;
import com.bancs.payments.repository.PaymentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentsRepository paymentsRepository;

    public List<TransactionDTO> getTransactionDetails(){
        List<Transaction> transactionList = paymentsRepository.findAll();
        return transactionList.stream().map(TransactionMapper::toDTO).toList();
    }
}
