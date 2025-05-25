package com.spring.batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class BatchJobController {

    private final JobLauncher jobLauncher;
    private final Job indiaPincodesJob;
    private final Job indiaPopulationJob;
    private final Job transactionjob;


    @Autowired
    public BatchJobController(JobLauncher jobLauncher, @Qualifier("indiaPincodesJob") Job indiaPincodesJob,
                              @Qualifier("indiaPopulationJob") Job indiaPopulationJob,
                              @Qualifier("transactionJob") Job transactionjob) {
        this.jobLauncher = jobLauncher;
        this.indiaPincodesJob = indiaPincodesJob;
        this.indiaPopulationJob = indiaPopulationJob;
        this.transactionjob = transactionjob;
    }

    @GetMapping("/run")
    public ResponseEntity<String> runBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis()) // ensure uniqueness
                    .toJobParameters();
            jobLauncher.run(indiaPincodesJob, jobParameters);
            return ResponseEntity.ok("Batch job started successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start batch job: " + e.getMessage());
        }
    }
    @GetMapping("transactions")
    public ResponseEntity<String> runIndiaPopulationJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis()) // ensure uniqueness
                    .toJobParameters();
            jobLauncher.run(transactionjob, jobParameters);
            return ResponseEntity.ok("Batch job started successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start batch job: " + e.getMessage());
        }
    }

    @GetMapping("indiaCensus")
    public ResponseEntity<String> transactionDataRead() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis()) // ensure uniqueness
                    .toJobParameters();
            jobLauncher.run(indiaPopulationJob, jobParameters);
            return ResponseEntity.ok("Batch job started successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start batch job: " + e.getMessage());
        }
    }
}
