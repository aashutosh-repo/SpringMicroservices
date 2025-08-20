package com.spring.customer;

import com.spring.customer.customer.CustomerDetails;
import com.spring.customer.customer.CustomerKey;
import com.spring.customer.customer.DocumentsDetails;
import com.spring.customer.dto.DocumentsDtlsDto;
import com.spring.customer.repository.DocumentsRepository;
import com.spring.customer.services.DocumentsServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DocumentsServicesTest {

    @Mock
    private DocumentsRepository documentsRepository;

    @InjectMocks
    private DocumentsServices documentsServices;

    private CustomerKey key;
    private DocumentsDetails documentsDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        key = new CustomerKey();
        key.setCustomerId("C123");
        key.setCustomerType(1);
        CustomerDetails details = new CustomerDetails();
        details.setCustomerId(key);

        documentsDetails = new DocumentsDetails();
        documentsDetails.setCustomer(details);
        documentsDetails.setDocId(101);
    }

    @Test
    void getDocumentsByCustomerId_whenDocumentExists_returnsDto() {
        // Arrange
        when(documentsRepository.findByCustomer_CustomerId_CustomerIdAndCustomer_CustomerId_CustomerType(
                key.getCustomerId(), key.getCustomerType()))
                .thenReturn(Optional.of(documentsDetails));

        // Act
        DocumentsDtlsDto result = documentsServices.getDocumentsByCustomerId(key);

        // Assert
        assertNotNull(result);
        assertEquals("C123",result.getCustId());
        verify(documentsRepository).findByCustomer_CustomerId_CustomerIdAndCustomer_CustomerId_CustomerType("C123", 1);
    }

    @Test
    void getDocumentsByCustomerId_whenDocumentNotFound_returnsEmptyDto() {
        // Arrange
        when(documentsRepository.findByCustomer_CustomerId_CustomerIdAndCustomer_CustomerId_CustomerType(
                key.getCustomerId(), key.getCustomerType()))
                .thenReturn(Optional.empty());

        // Act
        DocumentsDtlsDto result = documentsServices.getDocumentsByCustomerId(key);

        // Assert
        assertNotNull(result);
        assertNull(result.getDocIdentificationNumber());  // since it's empty DTO
        verify(documentsRepository).findByCustomer_CustomerId_CustomerIdAndCustomer_CustomerId_CustomerType("C123", 1);
    }

    @Test
    void saveDocumentDetails_savesDocument() {
        // Arrange
        // No return needed since repository.save returns the entity itself
        when(documentsRepository.save(documentsDetails)).thenReturn(documentsDetails);

        // Act
        documentsServices.saveDocumentDetails(documentsDetails);

        // Assert
        verify(documentsRepository).save(documentsDetails);
    }
}

