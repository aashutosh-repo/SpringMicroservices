package com.bancs.account.services.si;

import com.bancs.account.account.Account;
import com.bancs.account.dto.AccountDto;

import java.util.List;

public interface Account_Service_Interface {
	
	Account createModifyAccountDetails(AccountDto account, int modifyFlag);
	void deleteAccount(Account account);
	List<AccountDto> findAllAccounts();
	List<AccountDto> getAccountByCustomerId(String customerId);
	Account createAccountPendingAuth(AccountDto accountdDto);

}
