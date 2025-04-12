package com.spring.customer.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface Repositorytest extends JpaRepository<Test, Long> {
    @Query("SELECT a FROM Test a WHERE a.createdDate BETWEEN :startDate AND :endDate")
    List<CustomerDetails> findAccountsBetweenDates(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);
}
