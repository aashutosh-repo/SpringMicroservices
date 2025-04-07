package com.spring.core.services;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.spring.core.entity.IndianCities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class FileHandler {
    @Autowired
    private ResourceLoader resourceLoader;

    public List<IndianCities> citiesOfIndia(String filePath) throws IOException {
        List<IndianCities> indianCitiesList = new ArrayList<>();

        Resource resource = resourceLoader.getResource(filePath);
        try(CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))){
            String[] nextLine;
            boolean firstLine = true;

            while ((nextLine = csvReader.readNext()) != null){
                if(firstLine){
                    firstLine = false;
                    continue;
                }

                IndianCities cities = new IndianCities();
                cities.setCity(nextLine[0]);
                cities.setLat(nextLine[1]);
                cities.setLon(nextLine[2]);
                cities.setCountry(nextLine[3]);
                cities.setIso2(nextLine[4]);
                cities.setState(nextLine[5]);

                indianCitiesList.add(cities);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }
            return indianCitiesList.stream().sorted(Comparator.comparing(IndianCities::getState)).toList();
//        return indianCitiesList;
    }
}
