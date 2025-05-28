package com.bancs.payments.repository;

import com.bancs.payments.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentsRepository extends JpaRepository<Transaction,String> {
}
