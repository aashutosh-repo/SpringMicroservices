package com.spring.customer.repository;


import com.spring.customer.customer.CustomerDetails;
import com.spring.customer.customer.CustomerID;
import com.spring.customer.dto.CustomerDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface Customer_Details_Repository extends JpaRepository<CustomerDetails, CustomerID> {
	Optional<CustomerDetails> findByMobileNumber(String mobNumber);
	List<CustomerDetails> findByCustomerCategoryAndCustCreationDtBetween(String customerType, LocalDate startDate, LocalDate endDate);
}
