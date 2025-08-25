package com.spring.customer;


import com.spring.customer.customer.CustomerAddressDetails;
import com.spring.customer.customer.CustomerDetails;
import com.spring.customer.customer.CustomerKey;
import com.spring.customer.dto.CustomerAddressDto;
import com.spring.customer.repository.Customer_Address_Repository;
import com.spring.customer.repository.Customer_Details_Repository;
import com.spring.customer.services.CustomerAddressServices;
import com.spring.customer.utils.SequenceGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CustomerAddressServicesTest {
    @Mock
    private Customer_Address_Repository customerAddressRepository;

    @Mock
    private Customer_Details_Repository customerDetailsRepository;

    @Mock
    private SequenceGenerator sequenceGenerator;

    @InjectMocks
    private CustomerAddressServices customerAddressServices;

    private CustomerDetails customerDetails;
    private CustomerAddressDto customerAddressDto;
    private CustomerAddressDetails customerAddressDetails;

    @BeforeEach
    void setUp() {
        customerDetails = new CustomerDetails();
        CustomerKey customerKey = new CustomerKey("CUST001", 1);
        customerDetails.setCustomerId(customerKey);

        customerAddressDto = new CustomerAddressDto();
        customerAddressDto.setCity("Delhi");
        customerAddressDto.setPinCode(110001);

        customerAddressDetails = new CustomerAddressDetails();
        customerAddressDetails.setAddressId(101L);
        customerAddressDetails.setCity("Delhi");
    }

    @Test
    void testCreateModifyCustAddressDetails_WhenCustomerExists() {
        // Implement test logic here
        when(customerDetailsRepository.findByCustomerId(any(CustomerKey.class))).thenReturn(Optional.of(customerDetails));
        when(sequenceGenerator.generateSequence("AddressId_seq")).thenReturn(BigInteger.valueOf(101L));
        when(customerAddressRepository.saveAndFlush(any(CustomerAddressDetails.class))).thenReturn(customerAddressDetails);

        assertDoesNotThrow(() -> customerAddressServices.createModifyCustAddressDetails(customerAddressDto, customerDetails));
        verify(customerDetailsRepository,times(1)).findByCustomerId(any(CustomerKey.class));
        verify(customerAddressRepository,times(1)).saveAndFlush(any(CustomerAddressDetails.class));
    }
    @Test
    void testCreateModifyCustAddressDetails_WhenCustomerDoesNotExist() {
        when(customerDetailsRepository.findByCustomerId(any(CustomerKey.class))).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> customerAddressServices.createModifyCustAddressDetails(customerAddressDto, customerDetails));
        verify(customerDetailsRepository, times(1)).findByCustomerId(any(CustomerKey.class));
        verify(customerAddressRepository, times(0)).saveAndFlush(any(CustomerAddressDetails.class));
    }

    @Test
    void testFindCustAddressByID_WhenAddressExists() {
        when(customerAddressRepository.findById(101L))
                .thenReturn(Optional.of(customerAddressDetails));

        Optional<CustomerAddressDetails> result = customerAddressServices.findCustAddressByID(101L);

        assertTrue(result.isPresent());
        assertEquals(101L, result.get().getAddressId());
    }

    @Test
    void testFindCustAddressByID_WhenAddressDoesNotExist() {
        when(customerAddressRepository.findById(101L))
                .thenReturn(Optional.empty());

        Optional<CustomerAddressDetails> result = customerAddressServices.findCustAddressByID(101L);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteAddress() {
        doNothing().when(customerAddressRepository).delete(any(CustomerAddressDetails.class));

        boolean result = customerAddressServices.deleteAddress(customerAddressDetails);

        assertTrue(result);
        verify(customerAddressRepository, times(1)).delete(customerAddressDetails);
    }

    @Test
    void testFindAddressByCustomerID_WhenAddressExists() {
        when(customerAddressRepository.findByCustomer_CustomerId_CustomerIdAndCustomer_CustomerId_CustomerType("CUST001", 1))
                .thenReturn(Optional.of(customerAddressDetails));

        CustomerAddressDto result = customerAddressServices.findAddressByCustomerID("CUST001", 1);

        assertNotNull(result);
        assertEquals("Delhi", result.getCity());
    }

    @Test
    void testFindAddressByCustomerID_WhenAddressDoesNotExist() {
        when(customerAddressRepository.findByCustomer_CustomerId_CustomerIdAndCustomer_CustomerId_CustomerType("CUST001", 1))
                .thenReturn(Optional.empty());

        CustomerAddressDto result = customerAddressServices.findAddressByCustomerID("CUST001", 1);

        assertNotNull(result);
        assertNull(result.getCity()); // because it will return new CustomerAddressDto()
    }

}

