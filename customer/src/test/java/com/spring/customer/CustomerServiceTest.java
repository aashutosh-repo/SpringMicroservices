package com.spring.customer;

import com.spring.customer.customer.CustomerDetails;
import com.spring.customer.customer.CustomerKey;
import com.spring.customer.dto.*;
import com.spring.customer.error.ResourceNotFoundException;
import com.spring.customer.mapper.CustomerDetailsMapper;
import com.spring.customer.repository.Customer_Details_Repository;
import com.spring.customer.services.*;
import com.spring.customer.utils.SequenceGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock private Customer_Details_Repository detailsRepository;
    @Mock private CustomerAddressServices addressServices;
    @Mock private DocumentsServices documentsServices;
    @Mock private NomineeDetailsServices nomineeService;
    @Mock private SequenceGenerator sequenceGenerator;

    private CustomerDto customerDto;
    private CustomerAddressDto addressDto;
    private DocumentsDtlsDto documentsDto;
    List<NomineeDto> nomineeDtos;
    private CustomerKey key;
    private CustomerDetails customerEntity;

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

    @BeforeEach
    void setup() {
        customerDto = new CustomerDto();
        customerDto.setCustomerType("2");   // example type

        addressDto = new CustomerAddressDto();
        documentsDto = new DocumentsDtlsDto();
        nomineeDtos= new ArrayList<>();

        key = new CustomerKey("C123", 1);
        customerEntity = new CustomerDetails();
        customerEntity.setCustomerId(key);
        customerEntity.setFirstName("John");
        customerEntity.setLastName("Doe");
    }

    // ---------- Tests ----------
    //---- Creation -----
    @Test
    void testCreateCustomerDetails_Success(){
        //Step1: arrange
        BigInteger generatedId = BigInteger.valueOf(123);
        when(sequenceGenerator.generateSequence("CustomerID_seq")).thenReturn(generatedId);
        CustomerDetails details = new CustomerDetails();
        details.setCustomerId(new CustomerKey("CUST000123",2));
        when(detailsRepository.save(any(CustomerDetails.class))).thenReturn(details);

        //Step2: act

        service.CreateCustomerDetails(customerDto,addressDto,documentsDto, nomineeDtos);

        //step3: Assert
        verify(sequenceGenerator,times(1)).generateSequence("CustomerID_seq");
        verify(detailsRepository, times(1)).save(any(CustomerDetails.class));
        verify(addressServices, times(1)).createModifyCustAddressDetails(eq(addressDto), any(CustomerDetails.class));
        verify(nomineeService, times(1)).createNomineesDetails(eq(Collections.emptyList()), any(CustomerDetails.class), eq(1));
    }

    @Test
    void testCreateCustomerDetails_DefaultCustomerTypeWhenNull() {
        // Arrange
        customerDto.setCustomerType(null); // force null to test default
        when(sequenceGenerator.generateSequence("CustomerID_seq")).thenReturn(BigInteger.ONE);
        when(detailsRepository.save(any(CustomerDetails.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        service.CreateCustomerDetails(
                customerDto,
                addressDto,
                documentsDto,
                Collections.emptyList()
        );

        // Assert
        verify(detailsRepository).save(argThat(customer ->
                customer.getCustomerId().getCustomerType() == 1   // default applied
        ));
    }

    @Test
    void searchCustomers_whenAllFiltersProvided_returnsMappedResponseAndAppliesAllSpecs() {
        CustomerSearchRequestDTO req = new CustomerSearchRequestDTO();
        req.setCustomerId("C123");
        req.setName("Aashutosh");
        req.setStatus("ACTIVE");
        req.setStartDate(LocalDate.of(2023, 1, 1));
        req.setEndDate(LocalDate.of(2023, 12, 31));

        CustomerDetails entity = customer1();

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
            ResponseWrapperDto wrapper = out.getFirst();
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

    @Test
    void testSearchSingleCustomerData_Found() {
        // Arrange
        when(detailsRepository.findById(key)).thenReturn(Optional.of(customerEntity));

        when(addressServices.findAddressByCustomerID("C123", 1)).thenReturn(addressDto);

        when(documentsServices.getDocumentsByCustomerId(key)).thenReturn(documentsDto);

        NomineeDto nominee = new NomineeDto();
        when(nomineeService.findNomineeByCustomerId(key)).thenReturn(List.of(nominee));

        // Act
        ResponseWrapperDto response = service.searchSingleCustomerData("C123", "1");

        // Assert
        assertNotNull(response);
        assertNotNull(response.getCustomerDetails());
        assertEquals("John", response.getCustomerDetails().getFirstName());
        assertEquals(addressDto, response.getAddressDetails());
        assertEquals(documentsDto, response.getDocumentDetails());
        assertEquals(1, response.getNomineeDetails().size());

        verify(detailsRepository).findById(key);
        verify(addressServices).findAddressByCustomerID("C123", 1);
        verify(documentsServices).getDocumentsByCustomerId(key);
        verify(nomineeService).findNomineeByCustomerId(key);
    }

    @Test
    void testSearchSingleCustomerData_NotFound() {
        // Arrange
        when(detailsRepository.findById(key)).thenReturn(Optional.empty());

        // Act
        ResponseWrapperDto response = service.searchSingleCustomerData("C123", "1");

        // Assert
        assertNotNull(response);
        assertNull(response.getCustomerDetails()); // no customer
        assertNull(response.getAddressDetails());
        assertNull(response.getDocumentDetails());
        assertNull(response.getNomineeDetails());

        verify(detailsRepository).findById(key);
        verifyNoInteractions(addressServices, documentsServices, nomineeService);
    }

    @Test
    void testFindCustomerByMobileNumber_Found() {
        // Arrange
        when(detailsRepository.findByMobileNumber("9999999999"))
                .thenReturn(Optional.of(customerEntity));

        // Act
        CustomerDto dto = service.findCustomerByMobileNumber("9999999999");

        // Assert
        assertNotNull(dto);
        assertEquals("John", dto.getFirstName());
        verify(detailsRepository).findByMobileNumber("9999999999");
    }

    @Test
    void testFindCustomerByMobileNumber_NotFound() {
        // Arrange
        when(detailsRepository.findByMobileNumber("8888888888"))
                .thenReturn(Optional.empty());

        // Act + Assert
        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.findCustomerByMobileNumber("8888888888"));

        assertTrue(ex.getMessage().contains("Customer MobileNumber :8888888888"));
        verify(detailsRepository).findByMobileNumber("8888888888");
    }
}
