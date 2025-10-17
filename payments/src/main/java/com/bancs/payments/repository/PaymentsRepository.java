package com.bancs.payments.repository;

import com.bancs.payments.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentsRepository extends JpaRepository<Transaction,String> {
    Optional<Transaction> findByExtRefId(String extRefId);
//    Transaction findByTransa
}
