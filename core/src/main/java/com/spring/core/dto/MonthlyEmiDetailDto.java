package com.spring.core.dto;


import lombok.Data;

@Data
public class MonthlyEmiDetailDto {
    private int monthNumber;
    private double emi;
    private double principalComponent;
    private double interestComponent;
    private double remainingBalance;

    public MonthlyEmiDetailDto(int monthNumber, double emi, double principalComponent, double interestComponent, double remainingBalance) {
        this.monthNumber = monthNumber;
        this.emi = emi;
        this.principalComponent = principalComponent;
        this.interestComponent = interestComponent;
        this.remainingBalance = remainingBalance;
    }
}

