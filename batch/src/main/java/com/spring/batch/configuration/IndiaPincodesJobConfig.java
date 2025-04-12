package com.spring.batch.configuration;

import com.spring.batch.FileWriter.BatchFileWriter;
import com.spring.batch.demoentity.IndiaPincodes;
import com.spring.batch.listner.JobCompletionNotificationListener;
import com.spring.batch.processor.FileProcessor;
import com.spring.batch.reader.FileReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing(
        dataSourceRef = "batchDataSource",
        transactionManagerRef = "transactionManager"
)public class IndiaPincodesJobConfig {

    private final FileReader reader;
    private final FileProcessor processor;
    private final BatchFileWriter writer;
    private final JobCompletionNotificationListener listener;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Step indiaPincodesStep() {
        return new StepBuilder("indiaPincodesStep", jobRepository)
                .<IndiaPincodes, IndiaPincodes>chunk(1000, transactionManager)
                .reader(reader.reader())
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job indiaPincodesJob() {
        return new JobBuilder("indiaPincodesJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(indiaPincodesStep())
                .build();
    }
}
