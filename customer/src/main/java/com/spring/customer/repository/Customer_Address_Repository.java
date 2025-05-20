package com.spring.customer.repository;


import com.spring.customer.customer.AddressID;
import com.spring.customer.customer.CustomerAddressDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface Customer_Address_Repository extends JpaRepository<CustomerAddressDetails, Long> {
    Optional<CustomerAddressDetails> findByCustomer_CustomerId_CustomerIdAndCustomer_CustomerId_CustomerType(int customerId, int customerType);

}
