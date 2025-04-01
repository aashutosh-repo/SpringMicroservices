package com.bancs.account.services;

import com.bancs.account.account.Account;
import com.bancs.account.account.AccountPk;
import com.bancs.account.constants.AccountsConstants;
import com.bancs.account.dto.AccountDto;
import com.bancs.account.dto.CustomerDto;
import com.bancs.account.error.CustomErrorMessage;
import com.bancs.account.error.ErrorCode;
import com.bancs.account.mapper.AccountMapper;
import com.bancs.account.repository.Account_repository;
import com.bancs.account.services.si.Account_Service_Interface;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class Account_services implements Account_Service_Interface {

	private static final Logger logger = LogManager.getLogger(Account_services.class);

	public final Account_repository account_repository;
	private final SequenceGenerator sequenceGenerator;
    private final TempModifiedEntityServices tempModifiedEntityService;
	private final WebClient webClient;


	@Override
	public Account createAccountPendingAuth(AccountDto accountDto) {
		Account account = AccountMapper.mapToAccount(accountDto, new Account());
		CustomerDto customer = webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path("/customer/getCustomerByMobileNumber") // API path
						.queryParam("mobileNumber", "9876543210")  // Pass mobileNumber as query param
						.build()
				)
				.retrieve()
				.bodyToMono(CustomerDto.class)
				.block();
		BigInteger entityId = sequenceGenerator.generateSequence("AccountId_seq");
		BigInteger intAccNumber = sequenceGenerator.generateSequence("InternalAccNO_seq");
		BigInteger customerAccNumber = sequenceGenerator.generateSequence("CustomerAccNO_seq");
		String intAccNumStr = "OMEGA"+intAccNumber.toString();
		String customerAccountNum = "SB"+ customerAccNumber.toString();
		AccountPk accId = new AccountPk();
		accId.setAccount_id(entityId.intValue());
		account.setOwner_name(customer.getFirstName() +" " +customer.getLastName());
		account.setAccount_status(AccountsConstants.PENDING_AUTH);
		account.setClsr_dt(null);
		account.setClsr_reason(null);
		account.setAccountId(accId);
		account.setInternalAcntNumber(intAccNumStr);
		account.setAccountNumber(customerAccountNum);
		
		
		boolean isStored =
				tempModifiedEntityService.storeTransactionInTemp(
						account.getAccountNumber(),
				Account.class.getName(),
				account
	        );
		if(isStored) {
			logger.info("Data Stored Successfully into TempModifiedEntityServices");
		}
		
		return account;
		
	}
	
	
	public void authAccountPendingTransaction(Account account) {
		Account  account_create = tempModifiedEntityService.retrieveTransactionFromTemp
				(account.getAccountNumber(), Account.class.getName(), Account.class);
		if(account_create.getAccountId() == null) {
			throw new CustomErrorMessage("No data Found in tempModifiedEntity");
		}
		account_repository.save(account_create);
		
	}

	@Override
	@CachePut(value = "accounts", key = "#account.account_number")
	public Account createModifyAccountDetails(AccountDto account, int modifyFlag){
        logger.info("Entered int Creation Task of class : {}On : {}", this.getClass().getSimpleName(), LocalDate.now());
		//If Modify flag is 1 then primary key should pass as a User-input
		Account acc;
		Account accCreate;
		if(modifyFlag==1) {
			acc = account_repository.findByAccountNumber(account.getAccount_number());
			//check the given data is existing in DB or not if yes then proceed
			acc.setCurrency(account.getCurrency());
			acc.setOwner_name(account.getOwner_name());
			acc.setAccount_status(account.getAccount_status());
			acc.setClsr_dt(account.getClsr_dt());
			acc.setClsr_reason(acc.getClsr_reason());
            account_repository.save(acc);

        }
		else
		{
//			CustomerID cId = new CustomerID();
//			cId.setCustomerID(account.getCust_id());
//			cId.setCustomerType(account.getCus_type());
//
//			Optional<CustomerDetails> customer = custRepository.findById(cId);
//			if(customer.isEmpty()) {
//				throw new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND);
//			}

			BigInteger entityId = sequenceGenerator.generateSequence("AccountId_seq");
			BigInteger intAccNumber = sequenceGenerator.generateSequence("InternalAccNO_seq");
			BigInteger customerAccNumber = sequenceGenerator.generateSequence("CustomerAccNO_seq");
			String intAccNumStr = "OMEGA"+intAccNumber.toString();
			String customerAccountNum = "SB"+ customerAccNumber.toString();
			AccountPk accId = new AccountPk();
			accId.setAccount_id(entityId.intValue());
			accId.setAccount_type(AccountsConstants.SAVINGS_ACCOUNT);
			accCreate= AccountMapper.mapToAccount(account, new Account());
			accCreate.setClsr_dt(null);
			accCreate.setClsr_reason(null);
			accCreate.setAccountId(accId);
			accCreate.setInternalAcntNumber(intAccNumStr);
			accCreate.setAccountNumber(customerAccountNum);

			acc= account_repository.save(accCreate);
            logger.debug("Account Creation Completed :  {}On : {}", this.getClass().getSimpleName(), LocalDate.now());

		}
		return acc;
	}

	@Override
	@CacheEvict(value = "accounts", key = "#account.accountId")
	public void deleteAccount(Account account) {
        new Account();
        Account account_del;
		if(account_repository.findById(account.getAccountId()).isPresent()) {
			account_del = (account_repository.findById(account.getAccountId()).orElseThrow(()-> new CustomErrorMessage(ErrorCode.ACCOUNT_NOT_FOUND)));
			account_del.setAccount_status(2);
			account_del.setClsr_dt (account.getClsr_dt());
			account_repository.save(account_del);
		}

	}

	@Override
	@Cacheable(value = "accounts")
	public List<AccountDto> findAllAccounts() {
        logger.debug("Account Finding in :  {}On : {}", this.getClass().getSimpleName(), LocalDate.now());

		List<Account> account;
		List<AccountDto> accDto = new ArrayList<>();
		AccountDto accountDto;
		account = account_repository.findAll();
		for(Account acc: account) {
			accountDto = AccountMapper.mapToAccountDto(acc, new AccountDto());
			accDto.add(accountDto);
		}
        logger.debug("Account Finding completed in :  {}On : {}", this.getClass().getSimpleName(), LocalDate.now());

		return accDto;
	}

	@Override
	@Cacheable(value = "accountByCustomerId", key = "#customerId")
	public List<AccountDto> getAccountByCustomerId(int customerId){
		List<Account> accounts;
		accounts= account_repository.findByCustId(customerId);
		List<AccountDto> accountDtos = new ArrayList<>(List.of());
		for(Account ac : accounts){
			accountDtos.add(AccountMapper.mapToAccountDto(ac,new AccountDto()));
		}
		return accountDtos;
	}

}