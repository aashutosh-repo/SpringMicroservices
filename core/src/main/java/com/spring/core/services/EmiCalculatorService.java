package com.spring.core.services;


import com.spring.core.dto.EmiCalculationResponseDto;
import com.spring.core.dto.EmiRequestDto;
import com.spring.core.dto.EmiSummaryDto;
import com.spring.core.dto.MonthlyEmiDetailDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmiCalculatorService {

    public EmiCalculationResponseDto calculateEmi(EmiRequestDto emiRequestDto){
        double principle = emiRequestDto.getLoanAmount();
        double annualInterestRate = emiRequestDto.getAnnualInterestRate();
        int tenure = emiRequestDto.getTenureInYears();


        double monthlyInterestRate = annualInterestRate/1200;
        int totalMonths = tenure* 12;

        double emi;
        emi = (principle* monthlyInterestRate*Math.pow(1+ monthlyInterestRate, totalMonths))/(Math.pow(1 + monthlyInterestRate, totalMonths)-1);

        double remainingPrinciple = principle;
        double totalInterest = 0.0;

        List<MonthlyEmiDetailDto> monthlyEmiDetailDtos = new ArrayList<>();

        for(int month= 1; month <= totalMonths; month++){
            double interestForMonth = remainingPrinciple * monthlyInterestRate;
            double principleForMonth = emi - interestForMonth;
            remainingPrinciple -= principleForMonth;
            totalInterest +=interestForMonth;
            monthlyEmiDetailDtos.add( new MonthlyEmiDetailDto(
                    month,
                    round(emi),
                    round(principleForMonth),
                    round(interestForMonth),
                    round(Math.max(remainingPrinciple,0))
            ));
        }
        double totalPayment = principle+ totalInterest;

        EmiSummaryDto summaryDto = new EmiSummaryDto(
                round(principle),
                round(totalInterest),
                round(totalPayment),
                round(emi)
        );
        return new EmiCalculationResponseDto(summaryDto, monthlyEmiDetailDtos);
    }
    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
