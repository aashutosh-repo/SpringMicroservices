package com.spring.batch.configuration;

import com.spring.batch.FileWriter.IndiaPopulationWriter;
import com.spring.batch.FileWriter.TransactionWriter;
import com.spring.batch.demoentity.PopulationCensusIndia;
import com.spring.batch.demoentity.Transaction;
import com.spring.batch.listner.JobCompletionNotificationListener;
import com.spring.batch.processor.PopulationCensusProcessor;
import com.spring.batch.processor.TransactionProcessor;
import com.spring.batch.reader.IndiaPopulationReader;
import com.spring.batch.reader.TransactionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing(
        dataSourceRef = "batchDataSource",
        transactionManagerRef = "transactionManager"
)public class TransactionDataConfig {

    private final TransactionReader reader;
    private final TransactionProcessor processor;
    private final TransactionWriter writer;
    private final JobCompletionNotificationListener listener;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Step transactionDataStep() {
        return new StepBuilder("transactionDataStep", jobRepository)
                .<Transaction, Transaction>chunk(1000, transactionManager)
                .reader(reader.transactionsReader())
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(value = "transactionJob")
    public Job transactionJob() {
        return new JobBuilder("transactionJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(transactionDataStep())
                .build();
    }
}