package com.spring.batch.dto;

import lombok.Data;

@Data
public class ResponseDTO {
    private String message;
    private String uploadedFileId;
    private String jobId;
    private String status;
}
