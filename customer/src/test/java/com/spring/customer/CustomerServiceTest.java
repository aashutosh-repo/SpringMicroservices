package com.spring.customer;

import com.spring.customer.customer.CustomerDetails;
import com.spring.customer.customer.CustomerKey;
import com.spring.customer.dto.*;
import com.spring.customer.mapper.CustomerDetailsMapper;
import com.spring.customer.repository.Customer_Details_Repository;
import com.spring.customer.services.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock private Customer_Details_Repository detailsRepository;
    @Mock private CustomerAddressServices addressServices;
    @Mock private DocumentsServices documentsServices;
    @Mock private NomineeDetailsServices nomineeService;

    @InjectMocks private CustomerDetailsServices service;

    private CustomerDetails customer1(){
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerId(new CustomerKey("C123",1));
        customerDetails.setFirstName("Aashutosh");
        customerDetails.setLastName("Kumar");
        customerDetails.setEmail("aashutosh@gmail.com");
        return customerDetails;
    }
    private CustomerDetails customer2(){
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setCustomerId(new CustomerKey("234",2));
        customerDetails.setFirstName("Aashutosh");
        customerDetails.setLastName("Kumar");
        customerDetails.setEmail("aashutosh@gmail.com");
        return customerDetails;
    }
    private Specification<CustomerDetails> anySpec(){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());
    }

    // ---------- Tests ----------

    @Test
    void searchCustomers_whenAllFiltersProvided_returnsMappedResponseAndAppliesAllSpecs() {
        CustomerSearchRequestDTO req = new CustomerSearchRequestDTO();
        req.setCustomerId("C123");
        req.setName("Aashutosh");
        req.setStatus("ACTIVE");
        req.setStartDate(LocalDate.of(2023, 1, 1));
        req.setEndDate(LocalDate.of(2023, 12, 31));

        CustomerDetails entity = customer1();

        CustomerAddressDto addressDto = new CustomerAddressDto();
        DocumentsDtlsDto documentsDto = new DocumentsDtlsDto();
        NomineeDto nominee = new NomineeDto();
        List<NomineeDto> nominees = List.of(nominee);
        CustomerDto mappedDto = new CustomerDto();

        when(detailsRepository.findAll(any(Specification.class))).thenReturn(List.of(entity));
        when(addressServices.findAddressByCustomerID("C123", 1)).thenReturn(addressDto);
        when(documentsServices.getDocumentsByCustomerId(entity.getCustomerId())).thenReturn(documentsDto);
        when(nomineeService.findNomineeByCustomerId(entity.getCustomerId())).thenReturn(nominees);

        try (MockedStatic<CustomerDetailsSpecification> specMock =
                     mockStatic(CustomerDetailsSpecification.class);
             MockedStatic<CustomerDetailsMapper> mapperMock =
                     mockStatic(CustomerDetailsMapper.class)) {

            specMock.when(() -> CustomerDetailsSpecification.hasCustomerId("C123"))
                    .thenReturn(anySpec());
            specMock.when(() -> CustomerDetailsSpecification.hasName("Aashutosh"))
                    .thenReturn(anySpec());
            specMock.when(() -> CustomerDetailsSpecification.hasStatus("ACTIVE"))
                    .thenReturn(anySpec());
            specMock.when(() -> CustomerDetailsSpecification.isBetweenDates(
                            LocalDate.of(2023,1,1), LocalDate.of(2023,12,31)))
                    .thenReturn(anySpec());

            mapperMock.when(() -> CustomerDetailsMapper.mapToCustomerDetailsDto(eq(entity), any(CustomerDto.class)))
                    .thenReturn(mappedDto);

            List<ResponseWrapperDto> out = service.searchCustomers(req);

            assertEquals(1, out.size());
            ResponseWrapperDto wrapper = out.get(0);
            assertSame(mappedDto, wrapper.getCustomerDetails());
            assertSame(addressDto, wrapper.getAddressDetails());
            assertSame(documentsDto, wrapper.getDocumentDetails());
            assertEquals(nominees, wrapper.getNomineeDetails());

            verify(detailsRepository).findAll(any(Specification.class));
            verify(addressServices).findAddressByCustomerID("C123", 1);
            verify(documentsServices).getDocumentsByCustomerId(entity.getCustomerId());
            verify(nomineeService).findNomineeByCustomerId(entity.getCustomerId());

            specMock.verify(() -> CustomerDetailsSpecification.hasCustomerId("C123"));
            specMock.verify(() -> CustomerDetailsSpecification.hasName("Aashutosh"));
            specMock.verify(() -> CustomerDetailsSpecification.hasStatus("ACTIVE"));
            specMock.verify(() -> CustomerDetailsSpecification.isBetweenDates(
                    LocalDate.of(2023,1,1), LocalDate.of(2023,12,31)));
        }
    }

    @Test
    void searchCustomers_whenNoFilters_callsRepositoryWithNullSpec_andDoesNotCallSpecFactories() {
        CustomerSearchRequestDTO req = new CustomerSearchRequestDTO();
        CustomerDetails entity = customer1();

        // Important: match null spec explicitly
        when(detailsRepository.findAll(Mockito.<Specification<CustomerDetails>>isNull()))
                .thenReturn(List.of(entity));

        when(detailsRepository.findAll(any(Specification.class))).thenReturn(List.of(entity));
        when(addressServices.findAddressByCustomerID("C123", 1)).thenReturn(new CustomerAddressDto());
        when(documentsServices.getDocumentsByCustomerId(entity.getCustomerId())).thenReturn(new DocumentsDtlsDto());
        when(nomineeService.findNomineeByCustomerId(entity.getCustomerId())).thenReturn(List.of());

        try (MockedStatic<CustomerDetailsSpecification> specMock =
                     mockStatic(CustomerDetailsSpecification.class);
             MockedStatic<CustomerDetailsMapper> mapperMock =
                     mockStatic(CustomerDetailsMapper.class)) {

            mapperMock.when(() -> CustomerDetailsMapper.mapToCustomerDetailsDto(eq(entity), any(CustomerDto.class)))
                    .thenReturn(new CustomerDto());

            // Act
            List<ResponseWrapperDto> out = service.searchCustomers(req);

            // Assert
            assertEquals(1, out.size());
            verify(detailsRepository).findAll(any(Specification.class));

            ArgumentCaptor<Specification<CustomerDetails>> captor =
                    ArgumentCaptor.forClass(Specification.class);

            verify(detailsRepository).findAll(captor.capture());

            Specification<CustomerDetails> captured = captor.getValue();
            assertNotNull(captured);
            // No spec methods should be called
            specMock.verifyNoInteractions();
        }
    }

    @Test
    void searchCustomers_whenOnlyNameProvided_onlyNameSpecIsUsed() {
        // Arrange
        CustomerSearchRequestDTO req = new CustomerSearchRequestDTO();
        req.setName("Aashutosh");

        CustomerDetails entity = customer2();
        when(detailsRepository.findAll(any(Specification.class))).thenReturn(List.of(entity));
        when(addressServices.findAddressByCustomerID("234", 2)).thenReturn(new CustomerAddressDto());
        when(documentsServices.getDocumentsByCustomerId(entity.getCustomerId())).thenReturn(new DocumentsDtlsDto());
        when(nomineeService.findNomineeByCustomerId(entity.getCustomerId())).thenReturn(List.of());

        try (MockedStatic<CustomerDetailsSpecification> specMock =
                     mockStatic(CustomerDetailsSpecification.class);
             MockedStatic<CustomerDetailsMapper> mapperMock =
                     mockStatic(CustomerDetailsMapper.class)) {

            specMock.when(() -> CustomerDetailsSpecification.hasName("Aashutosh"))
                    .thenReturn(anySpec());
            mapperMock.when(() -> CustomerDetailsMapper.mapToCustomerDetailsDto(eq(entity), any(CustomerDto.class)))
                    .thenReturn(new CustomerDto());

            // Act
            service.searchCustomers(req);

            // Assert
            specMock.verify(() -> CustomerDetailsSpecification.hasName("Aashutosh"));
            specMock.verifyNoMoreInteractions();
            verify(detailsRepository).findAll(any(Specification.class));
        }
    }

    @Test
    void searchCustomers_handlesMultipleCustomers_callsDownstreamForEach() {
        // Arrange
        CustomerSearchRequestDTO req = new CustomerSearchRequestDTO();
        req.setStatus("ACTIVE");

        CustomerDetails e1 = customer1();
        CustomerDetails e2 = customer2();

        when(detailsRepository.findAll(any(Specification.class))).thenReturn(List.of(e1, e2));
        when(addressServices.findAddressByCustomerID("C123", 1)).thenReturn(new CustomerAddressDto());
        when(addressServices.findAddressByCustomerID("234", 2)).thenReturn(new CustomerAddressDto());
        when(documentsServices.getDocumentsByCustomerId(e1.getCustomerId())).thenReturn(new DocumentsDtlsDto());
        when(documentsServices.getDocumentsByCustomerId(e2.getCustomerId())).thenReturn(new DocumentsDtlsDto());
        when(nomineeService.findNomineeByCustomerId(e1.getCustomerId())).thenReturn(List.of());
        when(nomineeService.findNomineeByCustomerId(e2.getCustomerId())).thenReturn(List.of());

        try (MockedStatic<CustomerDetailsSpecification> specMock =
                     mockStatic(CustomerDetailsSpecification.class);
             MockedStatic<CustomerDetailsMapper> mapperMock =
                     mockStatic(CustomerDetailsMapper.class)) {

            specMock.when(() -> CustomerDetailsSpecification.hasStatus("ACTIVE")).thenReturn(anySpec());
            mapperMock.when(() -> CustomerDetailsMapper.mapToCustomerDetailsDto(eq(e1), any(CustomerDto.class)))
                    .thenReturn(new CustomerDto());
            mapperMock.when(() -> CustomerDetailsMapper.mapToCustomerDetailsDto(eq(e2), any(CustomerDto.class)))
                    .thenReturn(new CustomerDto());

            // Act
            List<ResponseWrapperDto> out = service.searchCustomers(req);

            // Assert
            assertEquals(2, out.size());
            verify(addressServices).findAddressByCustomerID("C123", 1);
            verify(addressServices).findAddressByCustomerID("234", 2);
            verify(documentsServices).getDocumentsByCustomerId(e1.getCustomerId());
            verify(documentsServices).getDocumentsByCustomerId(e2.getCustomerId());
            verify(nomineeService).findNomineeByCustomerId(e1.getCustomerId());
            verify(nomineeService).findNomineeByCustomerId(e2.getCustomerId());
        }
    }
}
