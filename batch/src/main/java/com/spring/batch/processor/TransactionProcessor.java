package com.spring.batch.processor;

import com.spring.batch.paymentEntity.Transaction;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TransactionProcessor implements ItemProcessor<Transaction, Transaction> {

    private static final Logger log = LogManager.getLogger(TransactionProcessor.class);
    private final Validator beanValidator;


    // This method processes each item (in this case each PopulationCensusIndia object) that is read by the reader
    @Override
    public Transaction process(Transaction item) throws Exception {
        Set<ConstraintViolation<Transaction>> v = beanValidator.validate(item);
        if (!v.isEmpty()) {
            String msg = v.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining("; "));
            throw new ValidationException("Bean validation failed: " + msg);
        }
        if (!Set.of("INR","USD","EUR").contains(item.getCurrency())) {
            throw new IllegalArgumentException("Unsupported currency: " + item.getCurrency());
        }
        log.info(item);
        return item; // Return the cleaned item
    }
}
