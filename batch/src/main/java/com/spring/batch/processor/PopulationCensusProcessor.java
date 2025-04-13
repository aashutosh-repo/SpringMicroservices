package com.spring.batch.processor;

import com.spring.batch.demoentity.PopulationCensusIndia;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PopulationCensusProcessor implements ItemProcessor<PopulationCensusIndia, PopulationCensusIndia> {

    private static final Logger log = LogManager.getLogger(PopulationCensusProcessor.class);
    private final FlatFileItemReader<PopulationCensusIndia> indiaPopulationCensusReader;

    // This method processes each item (in this case each PopulationCensusIndia object) that is read by the reader
    @Override
    public PopulationCensusIndia process(PopulationCensusIndia item) throws Exception {
        log.info(item);
        if (item.getPopulation() != null) {
            item.setPopulation(removeCommas(String.valueOf(item.getPopulation())));
        }
        if (item.getFemales() != null) {
            item.setFemales(removeCommas(String.valueOf(item.getFemales())));
        }
        if (item.getMales() != null) {
            item.setMales(removeCommas(String.valueOf(item.getMales())));
        }
        if (item.getRuralPopulation() != null) {
            item.setRuralPopulation(removeCommas(String.valueOf(item.getRuralPopulation())));
        }
        if (item.getUrbanPopulation() != null) {
            item.setUrbanPopulation(removeCommas(String.valueOf(item.getUrbanPopulation())));
        }
//        if (item.getAreaSqKm() != null) {
//            item.setAreaSqKm(Double.valueOf(removeCommas(String.valueOf(item.getAreaSqKm()))));
//        }
        if (item.getDecadalGrowthPercent() != null) {
            item.setDecadalGrowthPercent(item.getDecadalGrowthPercent());
        }

        return item; // Return the cleaned item
    }

    private Long removeCommas(String value) {
        return Long.parseLong(value.replace(",", ""));
    }

    private Double removeCommasFromDouble(String value) {
        return Double.parseDouble(value.replace(",", ""));
    }
}
