package com.spring.batch.reader;

import com.spring.batch.configuration.InputFileProperties;
import com.spring.batch.demoentity.PopulationCensusIndia;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IndiaPopulationReader {
    private final InputFileProperties inputFileProperties;
    private final GenericFlatFileReaderFactory factory;

    @Bean
    public FlatFileItemReader<PopulationCensusIndia> indiaPopulationCensusReader() {
        return factory.buildReader(
                inputFileProperties.getPath(),
                PopulationCensusIndia.class,
                new String[]{
                        "stateRank", "state", "capital", "population", "percentOfTotalPopulation", "males", "females",
                        "sexRatio", "literacyRate", "ruralPopulation", "urbanPopulation", "areaSqKm", "densityPerSqKm", "decadalGrowthPercent"
                },
                1
        );
    }
}
