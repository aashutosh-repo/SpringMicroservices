package com.spring.customer.controller;

import com.spring.customer.customer.AddressID;
import com.spring.customer.customer.CustomerDetails;
import com.spring.customer.dto.CustomerAddressDto;
import com.spring.customer.services.CustomerAddressServices;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Omega Bank Address Controller",
		description = "This Swagger containing CRUD operation for Address Management")
@RestController
@RequestMapping("/cust-address")
public class CustomerAddressController {
	
	@Autowired
	private CustomerAddressServices cusServices;
	
	@PostMapping("/create")
	public void createAddressDetails(@RequestBody CustomerAddressDto custAddress ) {
		cusServices.createModifyCustAddressDetails(custAddress, new CustomerDetails());
	}
	
	@PutMapping("/modify")
	public void modifyAddressDetails(@RequestBody CustomerAddressDto cust_address ) {
		cusServices.createModifyCustAddressDetails(cust_address, new CustomerDetails());
	}
	
	@GetMapping("/delete")
	public void deleteAddressDetails(@RequestBody CustomerAddressDto cust_address ) {
//		int add_id= cust_address.getADDRESS_ID();
		AddressID addresspk = new AddressID();
//		addresspk.setPIN_CODE(cust_address.getADDRESS_ID());
//		addresspk.setPIN_CODE(cust_address.getPIN_CODE());
		
		
	}

}
