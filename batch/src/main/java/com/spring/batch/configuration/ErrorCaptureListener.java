package com.spring.batch.configuration;

import com.spring.batch.paymentEntity.Transaction;
import jakarta.xml.bind.ValidationException;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.StepSynchronizationManager;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.context.annotation.Bean;// batch/SkipRetryConfig.java
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.RetryPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@StepScope
public class ErrorCaptureListener implements SkipListener<Transaction, Transaction> {
    private final JdbcTemplate jdbc;

    public ErrorCaptureListener(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Override public void onSkipInRead(Throwable t) { /* no-op */ }

    @Override
    public void onSkipInProcess(Transaction item, Throwable t) {
        writeError(item, t);
    }

    @Override
    public void onSkipInWrite(Transaction item, Throwable t) {
        writeError(item, t);
    }

    private void writeError(Transaction item, Throwable t) {
        StepExecution stepExecution = Objects.requireNonNull(StepSynchronizationManager.getContext()).getStepExecution();
        long lineNumber = stepExecution.getReadCount();
        String rawLine = item != null ? item.toString() :
                (t instanceof FlatFileParseException parseEx ? parseEx.getInput() : null);
        if (t instanceof FlatFileParseException parseEx) {
            lineNumber = parseEx.getLineNumber();
        }        jdbc.update("""
        INSERT INTO payments_error(job_execution_id, line_number, raw_line, error_code, error_message)
        VALUES (?, ?, ?, ?, ?)
      """,
                stepExecution.getJobExecutionId(),
                lineNumber,
                rawLine,
                t.getClass().getSimpleName(),
                t.getMessage()
        );
    }

    @Bean
    SkipPolicy skipPolicy() {
        return (t, skipCount) -> t instanceof ValidationException
                || t instanceof FlatFileParseException
                || t instanceof IllegalArgumentException;
    }

    @Bean
    RetryPolicy retryPolicy() {
        SimpleRetryPolicy p = new SimpleRetryPolicy();
        p.setMaxAttempts(3);
        return p;
    }
}
