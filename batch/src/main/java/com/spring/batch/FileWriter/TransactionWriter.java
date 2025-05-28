package com.spring.batch.FileWriter;

import com.spring.batch.paymentEntity.Transaction;
import com.spring.batch.repositories.payment.TranactionRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionWriter implements ItemWriter<Transaction> {
    private static final Logger log = LogManager.getLogger(TransactionWriter.class);

    private final TranactionRepository tranactionRepository;

    @Override
    public void write(Chunk<? extends Transaction> chunk) throws Exception {
        tranactionRepository.saveAllAndFlush(chunk);
    }
}
