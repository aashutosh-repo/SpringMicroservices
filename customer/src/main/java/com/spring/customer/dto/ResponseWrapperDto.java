package com.spring.customer.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseWrapperDto {
    private CustomerDto customerDetails;
    private DocumentsDtlsDto documentDetails;
    private CustomerAddressDto addressDetails;
    private List<NomineeDto> nomineeDetails;
}
