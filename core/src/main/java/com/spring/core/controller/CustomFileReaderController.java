package com.spring.core.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.spring.core.entity.IndianCities;
import com.spring.core.services.FileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("core/fileHandler")
public class CustomFileReaderController {
    @Autowired
    private FileHandler csvReader;

    @GetMapping("/locations")
    public List<IndianCities> getLocations() throws IOException, CsvValidationException {
        String filePath = "classpath:InputFiles/Indian_Cities_Database.csv"; // put your CSV file in resources
        return csvReader.citiesOfIndia(filePath);
    }
}
