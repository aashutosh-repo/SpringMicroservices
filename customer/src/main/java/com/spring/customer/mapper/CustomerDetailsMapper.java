package com.spring.customer.mapper;


import com.spring.customer.customer.CustomerDetails;
import com.spring.customer.dto.CustomerDto;

public class CustomerDetailsMapper {
    private CustomerDetailsMapper() {
    }

    public static CustomerDto mapToCustomerDetailsDto(CustomerDetails customerDetails, CustomerDto customerDetailsDto) {
		customerDetailsDto.setCustomerId(String.valueOf(customerDetails.getCustomerId().getCustomerId()));
        customerDetailsDto.setCustomerType(String.valueOf(customerDetails.getCustomerId().getCustomerType()));
        customerDetailsDto.setFirstName(customerDetails.getFirstName());
        customerDetailsDto.setLastName(customerDetails.getLastName());
        customerDetailsDto.setFatherName(customerDetails.getFatherName());
        customerDetailsDto.setMotherName(customerDetails.getLastName());
        customerDetailsDto.setMail(customerDetails.getEmail());
        customerDetailsDto.setCustomerCategory(customerDetails.getCustomerId().getCustomerType());
        customerDetailsDto.setMobileNumber(customerDetails.getMobileNumber());
        customerDetailsDto.setStatus(customerDetails.getStatus());
        customerDetailsDto.setOnboardingDate(customerDetails.getCustCreationDt());
        customerDetailsDto.setDateOfBirth(customerDetails.getDateOfBirth());
        customerDetailsDto.setCustClsngDt(customerDetails.getCustClsngDt());
        customerDetailsDto.setRiskProfile(customerDetails.getRiskProfile());
        customerDetailsDto.setRatingAgency(customerDetails.getRatingAgency());
        return customerDetailsDto;
	}
	public static CustomerDetails mapToCustomerDetails(CustomerDto customerDetailsDto, CustomerDetails customerDetails)
	{
		customerDetails.setFirstName(customerDetailsDto.getFirstName());
        customerDetails.setLastName(customerDetailsDto.getLastName());
        customerDetails.setFatherName(customerDetailsDto.getFatherName());
        customerDetails.setMotherName(customerDetailsDto.getLastName());
        customerDetails.setEmail(customerDetailsDto.getMail());
        customerDetails.setMobileNumber(customerDetailsDto.getMobileNumber());
        customerDetails.setStatus(customerDetailsDto.getStatus());
        customerDetails.setDateOfBirth(customerDetailsDto.getDateOfBirth());
        customerDetails.setCustClsngDt(customerDetailsDto.getCustClsngDt());
        customerDetails.setRiskProfile(customerDetailsDto.getRiskProfile());
        customerDetails.setRatingAgency(customerDetailsDto.getRatingAgency());
        
        return customerDetails;
	}
	

}
