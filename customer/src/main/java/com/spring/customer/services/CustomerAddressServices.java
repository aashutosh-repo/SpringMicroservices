package com.spring.customer.services;


import com.spring.customer.customer.CustomerAddressDetails;
import com.spring.customer.customer.CustomerDetails;
import com.spring.customer.customer.CustomerKey;
import com.spring.customer.dto.CustomerAddressDto;
import com.spring.customer.mapper.AddressMapper;
import com.spring.customer.repository.Customer_Address_Repository;
import com.spring.customer.repository.Customer_Details_Repository;
import com.spring.customer.services.si.AddressServiceInterface;
import com.spring.customer.utils.SequenceGenerator;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
public class CustomerAddressServices implements AddressServiceInterface {

    private final Customer_Address_Repository customerAddressRepository;
    private final Customer_Details_Repository customerDetailsRepository;
    private final SequenceGenerator sequenceGenerator;

    public CustomerAddressServices(Customer_Address_Repository customerAddressRepository, Customer_Details_Repository customerDetailsRepository, SequenceGenerator sequenceGenerator) {
        this.customerAddressRepository = customerAddressRepository;
        this.customerDetailsRepository = customerDetailsRepository;
        this.sequenceGenerator = sequenceGenerator;
    }

    public void createModifyCustAddressDetails(CustomerAddressDto customerAddressDto, CustomerDetails customerDetails){
        CustomerKey customerKey = new CustomerKey(customerDetails.getCustomerId().getCustomerId(),customerDetails.getCustomerId().getCustomerType());
        CustomerDetails customerDetails1 = customerDetailsRepository.findByCustomerId(customerKey).orElse(null);
        if(customerDetails1 != null) {
            CustomerAddressDetails customer_address_details;
            customer_address_details = AddressMapper.mapToCustomerAddress(customerAddressDto, new CustomerAddressDetails());
            customer_address_details.setCustomer(customerDetails);
            BigInteger addressId = sequenceGenerator.generateSequence("AddressId_seq");
            customer_address_details.setAddressId(addressId.longValue());
            customerAddressRepository.saveAndFlush(customer_address_details);
        }
	}
    	
    public Optional<CustomerAddressDetails> findCustAddressByID(Long addressID) {
        return customerAddressRepository.findById(addressID);
    }

    public boolean deleteAddress(CustomerAddressDetails customer_Address_Details) {
        customerAddressRepository.delete(customer_Address_Details);
        return true;
    }

    public CustomerAddressDto findAddressByCustomerID(String customerId, int customerType){
        Optional<CustomerAddressDetails> addressDetails = customerAddressRepository.findByCustomer_CustomerId_CustomerIdAndCustomer_CustomerId_CustomerType(customerId,customerType);
        return addressDetails.map(customerAddressDetails -> AddressMapper.mapToCustomerAddressDto(customerAddressDetails, new CustomerAddressDto())).orElseGet(CustomerAddressDto::new);
    }
}
