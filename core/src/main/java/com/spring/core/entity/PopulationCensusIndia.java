package com.spring.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PopulationCensusIndia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seqId;
    private int stateRank;
    private String state;
    private String capital;
    private Long population;
    private Double percentOfTotalPopulation;
    private Long males;
    private Long females;
    private int sexRatio;
    private Double literacyRate;
    private Long ruralPopulation;
    private Long urbanPopulation;
    private Double areaSqKm;
    private Double densityPerSqKm;
    private String decadalGrowthPercent;
}
