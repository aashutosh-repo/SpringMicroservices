package com.spring.customer.repository;


import com.spring.customer.customer.DocumentsDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface DocumentsRepository extends JpaRepository<DocumentsDetails, Integer> {
    Optional<DocumentsDetails> findByCustomer_CustomerId_CustomerIdAndCustomer_CustomerId_CustomerType(String customerId, int customerType);

}
