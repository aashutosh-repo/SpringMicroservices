package com.spring.customer.repository;


import com.spring.customer.customer.CustomerDetails;
import com.spring.customer.customer.CustomerID;
import com.spring.customer.dto.CustomerDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface Customer_Details_Repository extends JpaRepository<CustomerDetails, CustomerID>, JpaSpecificationExecutor<CustomerDetails> {
	Optional<CustomerDetails> findByMobileNumber(String mobNumber);
	List<CustomerDetails> findByCustomerCategoryAndCustCreationDtBetween(String customerType, LocalDate startDate, LocalDate endDate);

}
