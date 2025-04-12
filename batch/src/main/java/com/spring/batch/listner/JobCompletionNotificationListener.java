package com.spring.batch.listner;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        System.out.println("OfficeLocation batch job started...");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        System.out.println("OfficeLocation batch job completed with status: " + jobExecution.getStatus());
    }
}