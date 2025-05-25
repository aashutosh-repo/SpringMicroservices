package com.spring.batch.repositories.demography;

import com.spring.batch.demoentity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranactionRepository extends JpaRepository<Transaction,String> {

}
