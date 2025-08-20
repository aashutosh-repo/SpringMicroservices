package com.spring.customer.services.si;



import com.spring.customer.dto.*;

import java.util.List;

public interface CustomerServiceInterface {

	public void CreateCustomerDetails(CustomerDto inp_cust_details,
									  CustomerAddressDto customerDto,
									  DocumentsDtlsDto docDtls,
									  List<NomineeDto> nomineeDtoList);
	CustomerDto findCustomerByMobileNumber(String mobile);
	CustomerDto modifyCustomer(CustomerDto customerInp, String customerId,int customerType);
	List<CustomerDto> getCustomerList();

    List<ResponseWrapperDto> searchCustomers(CustomerSearchRequestDTO requestDTO);
}
