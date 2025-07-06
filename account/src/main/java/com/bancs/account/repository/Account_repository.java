package com.bancs.account.repository;


import com.bancs.account.account.Account;
import com.bancs.account.account.AccountPk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Account_repository extends JpaRepository<Account, AccountPk>{
 List<Account> findByCustId(String custId);
 Account findByAccountNumber(String accNumber);
}
