package com.spring.core.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EmiRequestDto {
    @NotNull(message = "Please Insert value greater than 0")
    private double loanAmount;
    @NotNull(message = "please Insert value greater than 0")
    private double annualInterestRate;
    @NotNull(message = "please Insert value greater than 0")
    private int tenureInYears;
    private LocalDate startDate;
    private int repaymentFrequency;
}
