package com.spring.batch.paymentEntity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Entity
@Data
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="file_type", length = 64)
    private String fileType;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "storage_path", nullable = false)
    private String storagePath;

    @Column(name = "uploaded_by", length = 128)
    private String uploadedBy;

    @Column(name = "uploaded_at", nullable = false)
    private OffsetDateTime uploadedAt = OffsetDateTime.now();

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "job_execution_id")
    private Long jobExecutionId;

    @Column(name = "status", nullable = false, length = 32)
    private String status;

    @Column(name = "total_rows")
    private Long totalRows = 0L;

    @Column(name = "processed_rows")
    private Long processedRows = 0L;

    @Column(name = "success_rows")
    private Long successRows = 0L;

    @Column(name = "failed_rows")
    private Long failedRows = 0L;

    @Column(name = "message")
    private String message;
}
