package com.spring.customer.services.si;



import com.spring.customer.dto.CustomerDto;
import com.spring.customer.dto.CustomerSearchRequestDTO;
import com.spring.customer.dto.DocumentsDtlsDto;
import com.spring.customer.dto.NomineeDto;

import java.time.LocalDate;
import java.util.List;

public interface CustomerServiceInterface {

	public void CreateCustDetails(CustomerDto inp_cust_details,
								  DocumentsDtlsDto docDtls, List<NomineeDto> nomineeDtoList);
	CustomerDto findCustomerByMobileNumber(String mobile);
	CustomerDto modifyCustomer(CustomerDto cuatomerInp, int CustomerId,int CustomerType);
	List<CustomerDto> getAllCust();

    List<CustomerDto> searchCustomers(CustomerSearchRequestDTO requestDTO);
}
