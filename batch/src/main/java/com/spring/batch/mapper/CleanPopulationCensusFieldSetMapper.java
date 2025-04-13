package com.spring.batch.mapper;

import com.spring.batch.demoentity.PopulationCensusIndia;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class CleanPopulationCensusFieldSetMapper implements FieldSetMapper<PopulationCensusIndia> {

    @Override
    public PopulationCensusIndia mapFieldSet(FieldSet fieldSet) {
        PopulationCensusIndia item = new PopulationCensusIndia();

        item.setStateRank(fieldSet.readInt("stateRank"));
        item.setState(fieldSet.readString("state"));
        item.setCapital(fieldSet.readString("capital"));
        item.setPopulation(parseLong(fieldSet.readString("population")));
        item.setPercentOfTotalPopulation(parseDouble(fieldSet.readString("percentOfTotalPopulation")));
        item.setMales(parseLong(fieldSet.readString("males")));
        item.setFemales(parseLong(fieldSet.readString("females")));
        item.setSexRatio(parseInteger(fieldSet.readString("sexRatio")));
        item.setLiteracyRate(parseDouble(fieldSet.readString("literacyRate")));
        item.setRuralPopulation(parseLong(fieldSet.readString("ruralPopulation")));
        item.setUrbanPopulation(parseLong(fieldSet.readString("urbanPopulation")));
        item.setAreaSqKm(parseDouble(fieldSet.readString("areaSqKm")));
        item.setDensityPerSqKm(parseDouble(fieldSet.readString("densityPerSqKm")));
        item.setDecadalGrowthPercent(fieldSet.readString("decadalGrowthPercent"));

        return item;
    }

    private Long parseLong(String value) {
        if (value == null) return null;
        return Long.parseLong(value.replace(",", "").trim());
    }
    private Integer parseInteger(String value) {
        if (value == null) return null;
        return Integer.parseInt(value.replace(",", "").trim());
    }

    private Double parseDouble(String value) {
        if (value == null) return null;
        return Double.parseDouble(value.replace(",", "").replace("%", "").trim());
    }
}

