package com.spring.customer.repository;


import com.spring.customer.customer.NomineeDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface Nominee_Repository extends JpaRepository<NomineeDetails, Integer> {
	List<NomineeDetails> findByOwnerId(int ownerId);
	Optional<NomineeDetails> findByNomineeRefNum(int nomineeRefNum);
	List<NomineeDetails> findByCustomer_CustomerId_CustomerIdAndCustomer_CustomerId_CustomerType(String customerId, int customerType);

}
