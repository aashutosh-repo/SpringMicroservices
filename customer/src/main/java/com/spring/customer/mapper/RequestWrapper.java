package com.spring.customer.mapper;


import com.spring.customer.dto.CustomerAddressDto;
import com.spring.customer.dto.CustomerDto;
import com.spring.customer.dto.DocumentsDtlsDto;
import com.spring.customer.dto.NomineeDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class RequestWrapper {
	private CustomerDto customerDto;
	private DocumentsDtlsDto docDto;
	private CustomerAddressDto customerAddress;
	private List<NomineeDto> nomineeDetails;
}
