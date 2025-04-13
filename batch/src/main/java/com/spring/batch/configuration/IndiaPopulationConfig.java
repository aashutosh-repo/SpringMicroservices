package com.spring.batch.configuration;

import com.spring.batch.FileWriter.BatchFileWriter;
import com.spring.batch.FileWriter.IndiaPopulationWriter;
import com.spring.batch.demoentity.IndiaPincodes;
import com.spring.batch.demoentity.PopulationCensusIndia;
import com.spring.batch.listner.JobCompletionNotificationListener;
import com.spring.batch.processor.FileProcessor;
import com.spring.batch.processor.PopulationCensusProcessor;
import com.spring.batch.reader.FileReader;
import com.spring.batch.reader.IndiaPopulationReader;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableBatchProcessing(
        dataSourceRef = "batchDataSource",
        transactionManagerRef = "transactionManager"
)public class IndiaPopulationConfig {

    private final IndiaPopulationReader reader;
    private final PopulationCensusProcessor processor;
    private final IndiaPopulationWriter writer;
    private final JobCompletionNotificationListener listener;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Step indiaPopulationStep() {
        return new StepBuilder("indiaPopulationStep", jobRepository)
                .<PopulationCensusIndia, PopulationCensusIndia>chunk(1000, transactionManager)
                .reader(reader.indiaPopulationCensusReader())
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(value = "indiaPopulationJob")
    public Job indiaPopulationJob() {
        return new JobBuilder("indiaPopulationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .start(indiaPopulationStep())
                .build();
    }
}