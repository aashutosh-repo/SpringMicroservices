package com.spring.core.services.si;

import java.time.LocalDate;
import java.util.List;

public interface HolidayServiceInterface {
    List<LocalDate> getHolidayList();
    LocalDate getValidWorkingDay(LocalDate inpDate,  int addType, int prd_to_add);
    LocalDate findAvailableDate(LocalDate dateOut);
    LocalDate adjustWeekendsDates(LocalDate inpDate);
    }
