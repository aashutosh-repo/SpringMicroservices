package com.spring.customer.controller;


import com.spring.customer.constants.AccountsConstants;
import com.spring.customer.dto.CustomerDto;
import com.spring.customer.dto.CustomerSearchRequestDTO;
import com.spring.customer.dto.ResponseDto;
import com.spring.customer.mapper.RequestWrapper;
import com.spring.customer.services.CustomerAddressServices;
import com.spring.customer.services.CustomerDetailsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Omega Bank Customer Details Controller",
description = "This Swagger containing CRUD operation for Customer Management")
@RestController
@RequestMapping("/customer")
@AllArgsConstructor
public class CustomerDtlsController {
    CustomerDetailsServices customerDetailsServices;
    CustomerAddressServices custAddServices;

    @Operation(summary = "Find All Customer API",
    description = "REST API to All Customer in Omega Bank")
    @GetMapping("/getAllCustomerDetails")
    public List<CustomerDto> getAllCustomer(){
        return customerDetailsServices.getAllCust();
    }
    @Operation(summary = "Find Customer By Mobile Number API",
    description = "REST API to Find Customer By Mobile Number in Omega Bank")
    @GetMapping("/getCustomerByMobileNumber")
    public ResponseEntity<CustomerDto> geCustomerByCustomerId(@RequestParam String mobileNumber){
        return ResponseEntity.ok(customerDetailsServices.findCustomerByMobileNumber(mobileNumber));
    }
    
    @Operation(summary = "Create Customer API",
    description = "REST API to create Cutomer in Omega Bank")
    @ApiResponse(
            responseCode = "202",
            description = "Http Status CREATED"
    )
    @PostMapping("/customer-Onboarding")
    public ResponseEntity<ResponseDto> creteCust(@RequestBody RequestWrapper requestWrapper)
	{
    	customerDetailsServices.CreateCustDetails(requestWrapper.getCustomerDto(), 
    			requestWrapper.getDocDto(),requestWrapper.getNomineeDetails());
    	custAddServices.createModifyCustAddressDetails(requestWrapper.getCustomerAddress());
        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_202,AccountsConstants.MESSAGE_202));
    }
    
    
    @Operation(summary = "Modify Customer Details API",
    description = "REST API to Modify Customer Details in Omega Bank")
    @ApiResponse(
            responseCode = "201",
            description = "Http Status Succesfully Precessed"
    )
    @PutMapping("/modify-Customer")
    public ResponseEntity<ResponseDto> modifyDetails(@RequestBody CustomerDto customerDetails, 
    		@RequestParam int cust_id, @RequestParam int CustomerType){
    	customerDetailsServices.modifyCustomer(customerDetails, cust_id, CustomerType);
    	
    	return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(AccountsConstants.STATUS_200,AccountsConstants.MESSAGE_200));
    }

    @PostMapping("/customerSearch")
    public ResponseEntity<List<CustomerDto>> getCustomerDetails(@RequestBody CustomerSearchRequestDTO requestDTO){
        List<CustomerDto> customerDtoList = customerDetailsServices.searchCustomers(requestDTO);
        return  ResponseEntity.ok(customerDtoList);
    }
    @PostMapping("/customerSearchbydate")
    public ResponseEntity<List<CustomerDto>> getCustomerDetailsBydate(@RequestParam  CustomerSearchRequestDTO requestDTO){
        List<CustomerDto> customerDtoList = customerDetailsServices.searchCustomers(requestDTO);
        return  ResponseEntity.ok(customerDtoList);
    }

    
    
}



