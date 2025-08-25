package com.spring.customer;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import java.util.*;

import com.spring.customer.customer.CustomerDetails;
import com.spring.customer.customer.CustomerKey;
import com.spring.customer.customer.NomineeDetails;
import com.spring.customer.dto.NomineeDto;
import com.spring.customer.error.ResourceNotFoundException;
import com.spring.customer.repository.Nominee_Repository;
import com.spring.customer.services.NomineeDetailsServices;
import com.spring.customer.utils.SequenceGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NomineeDetailsServicesTest {

    @Mock
    private Nominee_Repository nomineeRepository;

    @Mock
    private SequenceGenerator sequenceGenerator;

    @InjectMocks
    private NomineeDetailsServices nomineeDetailsServices;

    private CustomerDetails customerDetails;
    private NomineeDto nomineeDto;
    private NomineeDetails nomineeDetails;

    @BeforeEach
    void setUp() {
        customerDetails = new CustomerDetails();
        CustomerKey key = new CustomerKey("CUST001", 1);
        customerDetails.setCustomerId(key);

        nomineeDto = new NomineeDto();
        nomineeDto.setNomineeRefNum(123);
        nomineeDto.setNomAddId(100);
        nomineeDto.setNomineeFirstName("Aashutosh kumar");

        nomineeDetails = new NomineeDetails();
        nomineeDetails.setNomineeId(100);
        nomineeDetails.setNomineeRefNum(123);
        nomineeDetails.setNomineeFirstName("Aashutosh kumar");
        nomineeDetails.setCustomer(customerDetails);
    }

    /** ✅ Test createNomineesDetails */
    @Test
    void testCreateNomineesDetails_ShouldCreateNominees() {
        when(sequenceGenerator.generateSequence("NomineeId_seq")).thenReturn(BigInteger.valueOf(100));
        when(nomineeRepository.save(any(NomineeDetails.class))).thenReturn(nomineeDetails);

        List<NomineeDto> inputList = List.of(nomineeDto);

        List<NomineeDto> result = nomineeDetailsServices.createNomineesDetails(inputList, customerDetails, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Aashutosh kumar", result.get(0).getNomineeFirstName());
        verify(nomineeRepository, times(1)).save(any(NomineeDetails.class));
    }

    /** ✅ Test modifyNomineeDetails - When nominee exists */
    @Test
    void testModifyNomineeDetails_WhenNomineeExists() {
        when(nomineeRepository.findByNomineeRefNum(123)).thenReturn(Optional.of(nomineeDetails));
        when(nomineeRepository.save(any(NomineeDetails.class))).thenReturn(nomineeDetails);

        List<NomineeDto> inputList = List.of(nomineeDto);

        List<NomineeDto> result = nomineeDetailsServices.modifyNomineeDetails(inputList, 0);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Aashutosh kumar", result.get(0).getNomineeFirstName());
        verify(nomineeRepository, times(1)).findByNomineeRefNum(123);
        verify(nomineeRepository, times(1)).save(any(NomineeDetails.class));
    }

    /** ✅ Test modifyNomineeDetails - When nominee does not exist */
    @Test
    void testModifyNomineeDetails_WhenNomineeDoesNotExist() {
        when(nomineeRepository.findByNomineeRefNum(123)).thenReturn(Optional.empty());

        List<NomineeDto> inputList = List.of(nomineeDto);

        List<NomineeDto> result = nomineeDetailsServices.modifyNomineeDetails(inputList, 0);

        assertTrue(result.isEmpty());
        verify(nomineeRepository, times(1)).findByNomineeRefNum(123);
        verify(nomineeRepository, never()).save(any());
    }

    /** ✅ Test deleteNominee - When nominee exists */
    @Test
    void testDeleteNominee_WhenNomineeExists() {
        when(nomineeRepository.findById(100)).thenReturn(Optional.of(nomineeDetails));

        nomineeDetailsServices.deleteNominee(nomineeDto);

        verify(nomineeRepository, times(1)).deleteById(100);
    }

    /** ✅ Test deleteNominee - When nominee does not exist */
    @Test
    void testDeleteNominee_WhenNomineeDoesNotExist() {
        when(nomineeRepository.findById(100)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> nomineeDetailsServices.deleteNominee(nomineeDto));

        assertTrue(exception.getMessage().contains("Nominee Not Existing"));
        verify(nomineeRepository, never()).deleteById(anyInt());
    }

    /** ✅ Test findNomineeByOwnerId - When nominees exist */
    @Test
    void testFindNomineeByOwnerId_WhenNomineesExist() {
        when(nomineeRepository.findByOwnerId(1)).thenReturn(List.of(nomineeDetails));

        List<NomineeDto> result = nomineeDetailsServices.findNomineeByOwnerId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Aashutosh kumar", result.get(0).getNomineeFirstName());
    }

    /** ✅ Test findNomineeByOwnerId - When nominees do not exist */
    @Test
    void testFindNomineeByOwnerId_WhenNoNominees() {
        when(nomineeRepository.findByOwnerId(1)).thenReturn(Collections.emptyList());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> nomineeDetailsServices.findNomineeByOwnerId(1));

        assertTrue(exception.getMessage().contains("Nominee Not Found"));
    }

    /** ✅ Test findNomineeByCustomerId */
    @Test
    void testFindNomineeByCustomerId() {
        when(nomineeRepository.findByCustomer_CustomerId_CustomerIdAndCustomer_CustomerId_CustomerType("CUST001", 1))
                .thenReturn(List.of(nomineeDetails));

        CustomerKey key = new CustomerKey("CUST001", 1);
        List<NomineeDto> result = nomineeDetailsServices.findNomineeByCustomerId(key);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Aashutosh kumar", result.get(0).getNomineeFirstName());
    }
}
