package com.spring.customer.repository;


import com.spring.customer.customer.DocumentsDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface DocumentsRepository extends JpaRepository<DocumentsDetails, Integer> {

}
