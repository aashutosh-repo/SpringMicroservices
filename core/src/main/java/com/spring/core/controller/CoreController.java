package com.spring.core.controller;


import com.spring.core.dto.EmiCalculationResponseDto;
import com.spring.core.dto.EmiRequestDto;
import com.spring.core.services.EmiCalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/core/instruments")
@RequiredArgsConstructor
public class CoreController {
    private final EmiCalculatorService emiCalculatorService;

    @PostMapping("/emiCalculator")
    public ResponseEntity<EmiCalculationResponseDto> emiCalculator(@RequestBody EmiRequestDto requestDto){
        EmiCalculationResponseDto responseDto =  emiCalculatorService.calculateEmi(requestDto);
        return ResponseEntity.ok(responseDto);
    }

}
