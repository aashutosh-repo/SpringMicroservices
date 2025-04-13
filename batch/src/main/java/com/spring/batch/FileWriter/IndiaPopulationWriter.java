package com.spring.batch.FileWriter;

import com.spring.batch.demoentity.PopulationCensusIndia;
import com.spring.batch.repositories.demography.IndiaPopulationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IndiaPopulationWriter implements ItemWriter<PopulationCensusIndia> {
    private static final Logger log = LogManager.getLogger(IndiaPopulationWriter.class);

    private final IndiaPopulationRepository populationRepository;

    @Override
    public void write(Chunk<? extends PopulationCensusIndia> chunk) throws Exception {
        populationRepository.saveAllAndFlush(chunk);
        log.info(chunk);
    }
}
