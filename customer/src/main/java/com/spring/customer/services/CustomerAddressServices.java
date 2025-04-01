package com.spring.customer.services;


import com.spring.customer.customer.AddressID;
import com.spring.customer.customer.CustomerAddressDetails;
import com.spring.customer.dto.CustomerAddressDto;
import com.spring.customer.mapper.AddressMapper;
import com.spring.customer.repository.Customer_Address_Repository;
import com.spring.customer.services.si.AddressServiceInterface;
import com.spring.customer.utils.SequenceGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerAddressServices implements AddressServiceInterface {

    private Customer_Address_Repository customerAddressRepository;
    private SequenceGenerator sequenceGenerator;
    
    public void createModifyCustAddressDetails(CustomerAddressDto customerAddressDto){
        new CustomerAddressDetails();
            AddressID addressKey = new AddressID();
            CustomerAddressDetails customer_address_details;
            customer_address_details = AddressMapper.mapToCustomerAddress(customerAddressDto, new CustomerAddressDetails());
            BigInteger addressId = sequenceGenerator.generateSequence("AddressId_seq");
            addressKey.setCustomerId(customerAddressDto.getCustomerID());
            addressKey.setAddressId(addressId.intValue());
            customer_address_details.setAddressId(addressKey);
            customerAddressRepository.save(customer_address_details);
	}
    	
    	public Optional<CustomerAddressDetails> findCustAddressByID(AddressID addressID) {
    		return customerAddressRepository.findById(addressID);
    	}
    	public boolean deleteAddress(CustomerAddressDetails customer_Address_Details) {
    		customerAddressRepository.delete(customer_Address_Details);
    		return true;
    	}

}
