package com.bancs.account.controller;

import com.bancs.account.account.Account;
import com.bancs.account.constants.AccountsConstants;
import com.bancs.account.dto.AccountDto;
import com.bancs.account.dto.CustomerDto;
import com.bancs.account.dto.ResponseDto;
import com.bancs.account.services.Account_services;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;


@Tag(name = "Omega Bank Account Controller",
description = "This Swagger containing CRUD operation for Account Management")
@RestController
@RequestMapping("/account")
@AllArgsConstructor
public class AccountController {
	@Autowired
	public Account_services acc_services;
    private final WebClient webClient;


    @Operation(summary = "View All Accounts API",
    description = "REST API to View All account in MyBank")
    @GetMapping("/viewAccounts")
    public ResponseEntity<List<AccountDto>> viewAccountDetails(){
        return new ResponseEntity<>(acc_services.findAllAccounts(),HttpStatus.OK);
    }

    @Operation(summary = "View All Accounts API",
            description = "REST API to View account By CustomerId in MyBank")
    @GetMapping("/viewAccountByCustomerId")
    public List<AccountDto> viewAccountByCustomerId(int customerId){
        return acc_services.getAccountByCustomerId(customerId);
    }
    @Operation(summary = "Create Account API",
    description = "REST API to create account in Omega Bank")
    @ApiResponse(
            responseCode = "201",
            description = "Http Status CREATED"
    )
    @PostMapping("/create")
    public ResponseEntity<ResponseDto> createAccount(@RequestBody AccountDto account, @RequestParam String MobileNum) {
    	System.out.println(account.toString());
    	acc_services.createModifyAccountDetails(account, 0);
    	return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }
    
    @Operation(summary = "Modify Account API",
    description = "REST API to Modify Details in Omega Bank")
    @ApiResponse(
            responseCode = "201",
            description = "Http Status Succesfully Precessed"
    )
    @PutMapping("/modify")
    public ResponseEntity<ResponseDto> modifyAccount(@RequestBody AccountDto account) {
    	Account accountOut = acc_services.createModifyAccountDetails(account, 1);
    	return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
    }

    @GetMapping("/test")
    public CustomerDto testM(@RequestParam String mobNumber){
        CustomerDto customer = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/customer/getCustomerByMobileNumber") // API path
                        .queryParam("mobileNumber", "9876543210")  // Pass mobileNumber as query param
                        .build()
                )
                .retrieve()
                .bodyToMono(CustomerDto.class)
                .block();
        return customer;
    }
}
