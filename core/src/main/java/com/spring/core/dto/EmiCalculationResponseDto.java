package com.spring.core.dto;

import lombok.Data;

import java.util.List;

@Data
public class EmiCalculationResponseDto {
    private EmiSummaryDto summary;
    private List<MonthlyEmiDetailDto> monthlyBreakdown;

    public EmiCalculationResponseDto(EmiSummaryDto summary, List<MonthlyEmiDetailDto> monthlyBreakdown) {
        this.summary = summary;
        this.monthlyBreakdown = monthlyBreakdown;
    }
}
