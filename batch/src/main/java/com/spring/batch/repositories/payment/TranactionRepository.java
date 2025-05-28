package com.spring.batch.repositories.payment;

import com.spring.batch.paymentEntity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranactionRepository extends JpaRepository<Transaction,String> {

}
