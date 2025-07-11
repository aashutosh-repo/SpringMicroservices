package com.spring.core.dto;

import lombok.Data;

@Data
public class EmiSummaryDto {
    private double totalPrincipal;
    private double totalInterest;
    private double totalAmountPayable;
    private double monthlyEmi;

    public EmiSummaryDto(double totalPrincipal, double totalInterest, double totalAmountPayable, double monthlyEmi) {
        this.totalPrincipal = totalPrincipal;
        this.totalInterest = totalInterest;
        this.totalAmountPayable = totalAmountPayable;
        this.monthlyEmi = monthlyEmi;
    }
}

