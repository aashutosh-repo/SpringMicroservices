package com.spring.customer.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerSearchRequestDTO {
    private String customerType;
    private LocalDate startDate;
    private LocalDate endDate;
    private String name;
    private String customerId;
    private String status;
}
