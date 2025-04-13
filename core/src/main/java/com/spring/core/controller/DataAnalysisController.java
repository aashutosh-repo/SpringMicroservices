package com.spring.core.controller;

import com.spring.core.entity.PopulationCensusIndia;
import com.spring.core.repository.IndiaPopulationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/core/analysis")
@RequiredArgsConstructor
public class DataAnalysisController {
    private final IndiaPopulationRepository populationRepository;

    @GetMapping("/census")
    public ResponseEntity<List<PopulationCensusIndia>> getCesusData(){
        List<PopulationCensusIndia> censusIndias= populationRepository.findAll();
        return ResponseEntity.ok(censusIndias);
    }
}
