package com.spring.customer.customer;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Test {
    @Id
    private Long id;
    private long pin_code;
    private LocalDate createdDate;
}
