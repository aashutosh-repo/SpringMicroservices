package com.spring.batch.processor;

import com.spring.batch.paymentEntity.Transaction;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransactionProcessor implements ItemProcessor<Transaction, Transaction> {

    private static final Logger log = LogManager.getLogger(TransactionProcessor.class);
    private final FlatFileItemReader<Transaction> indiaPopulationCensusReader;

    // This method processes each item (in this case each PopulationCensusIndia object) that is read by the reader
    @Override
    public Transaction process(Transaction item) throws Exception {
        log.info(item);
        return item; // Return the cleaned item
    }
}
