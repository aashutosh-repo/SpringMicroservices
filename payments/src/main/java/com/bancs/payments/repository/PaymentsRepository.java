package com.bancs.payments.repository;

import com.bancs.payments.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends JpaRepository<Transaction,String> {
}
