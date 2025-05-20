package com.spring.customer.error;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Data
public class ErrorResponse {
	private String path;
    private HttpStatus errorCode;
    private String errorDescription;
    private LocalDateTime timestamp;
}