package com.spring.core.repository;

import com.spring.core.utils.Holidays;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface HolidayRepository extends JpaRepository<Holidays, Long>{
    boolean existsByDate(LocalDate date);
}
