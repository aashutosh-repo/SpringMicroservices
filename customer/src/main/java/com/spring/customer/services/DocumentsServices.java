package com.spring.customer.services;

import com.spring.customer.configuration.OpenAPIConfig;
import com.spring.customer.customer.CustomerKey;
import com.spring.customer.customer.DocumentsDetails;
import com.spring.customer.dto.DocumentsDtlsDto;
import com.spring.customer.mapper.DocumentDetailsMapper;
import com.spring.customer.repository.DocumentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DocumentsServices {
    private final DocumentsRepository documentsRepository;

    public DocumentsDtlsDto getDocumentsByCustomerId(CustomerKey key){
        Optional<DocumentsDetails> documentsDetails= documentsRepository.findByCustomer_CustomerId_CustomerIdAndCustomer_CustomerId_CustomerType(key.getCustomerId(), key.getCustomerType());
        return documentsDetails.map(customerDocument -> DocumentDetailsMapper.mapToDocumentDetailsDto(customerDocument,new DocumentsDtlsDto())).orElseGet(DocumentsDtlsDto::new );
    }
}
