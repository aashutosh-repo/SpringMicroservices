package com.spring.core.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/core/holidays")
public class HolidaysDateController {

    @GetMapping("/getHolidays")
    public ResponseEntity<List<LocalDate>> getHolidaysDates(){
        LocalDate today = LocalDate.now();
        LocalDate today1 = LocalDate.now().plusDays(1);
        List<LocalDate> dates = new ArrayList<>(Arrays.asList(today1,today));
        return ResponseEntity.ok(dates);
    }
}
