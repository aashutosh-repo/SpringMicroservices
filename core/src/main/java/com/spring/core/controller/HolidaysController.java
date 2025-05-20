package com.spring.core.controller;

import com.opencsv.exceptions.CsvValidationException;
import com.spring.core.services.HolidayServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/core/v1/holidays")
@RequiredArgsConstructor
public class HolidaysController {

    private final HolidayServices holidayservices;

    @GetMapping("getNextValidDate")
    public ResponseEntity<LocalDate> getValidDate(@RequestParam LocalDate dateinput,
                                                  @RequestParam int paramtoAdd, @RequestParam int qtyToAdd ){
        LocalDate date = holidayservices.getValidWorkingDay(dateinput,paramtoAdd,qtyToAdd);
        return ResponseEntity.ok(date);
    }

    @GetMapping("getHolidays")
    public ResponseEntity<List<LocalDate>> getHolidaysDates(){
        List<LocalDate> allHolidays =  holidayservices.getHolidayList();
        return ResponseEntity.ok(allHolidays);
    }

    @GetMapping("/load-holidays")
    public String loadHolidays() throws CsvValidationException {
        try {
            holidayservices.loadHolidaysFromCSV();
            return "Holidays successfully loaded!";
        } catch (IOException e) {
            return "Failed to load holidays: " + e.getMessage();
        }
    }
}
