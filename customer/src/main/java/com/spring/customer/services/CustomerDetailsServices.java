package com.spring.customer.services;


import com.spring.customer.customer.*;
import com.spring.customer.dto.*;
import com.spring.customer.error.CustomerNotFoundException;
import com.spring.customer.error.ErrorCode;
import com.spring.customer.error.ErrorResponse;
import com.spring.customer.error.ResourceNotFoundException;
import com.spring.customer.mapper.CustomerDetailsMapper;
import com.spring.customer.mapper.DocumentDetailsMapper;
import com.spring.customer.repository.Customer_Details_Repository;
import com.spring.customer.repository.DocumentsRepository;
import com.spring.customer.services.si.CustomerServiceInterface;
import com.spring.customer.utils.SequenceGenerator;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class CustomerDetailsServices implements CustomerServiceInterface {

	@InitBinder
	public void initBinder(WebDataBinder webdataBinder) {
		//This method is to trim all spaces from start and end of string 
		StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
		webdataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
	}

	private final Customer_Details_Repository detailsRepository;
	private final DocumentsRepository docRepository;
	private final DocumentsServices documentsServices;
	private final CustomerAddressServices addressServices;
	private final NomineeDetailsServices nomineeService;
	private final SequenceGenerator sequenceGenerator;


	@Override
	@CacheEvict(value = "allCustomersCache", key = "'allCustomers'", allEntries = false)
	public void CreateCustomerDetails(CustomerDto inp_cust_details,
									  CustomerAddressDto addressDetailsDto,
									  DocumentsDtlsDto documentsDtlsDto,
									  List<NomineeDto> nomineeDtoList) {

        CustomerDetails customer_Details;
        DocumentsDetails doc;
		//Create customer Details
		customer_Details = CustomerDetailsMapper.mapToCustomerDetails(inp_cust_details, new CustomerDetails());
		customer_Details.setCustClsngDt(null);
		doc = DocumentDetailsMapper.mapToDocumentDetails(documentsDtlsDto, new DocumentsDetails());

		BigInteger entityId = sequenceGenerator.generateSequence("CustomerID_seq");
		CustomerKey customerKeyKey = new CustomerKey();
		customerKeyKey.setCustomerId("CUST000"+entityId.toString());
		if(inp_cust_details.getCustomerType() == null){
			customerKeyKey.setCustomerType(1);
		}else{
			customerKeyKey.setCustomerType(inp_cust_details.getCustomerCategory());
		}
		customer_Details.setCustomerId(customerKeyKey);
		customer_Details.setCustCreationDt(LocalDate.now());
		customer_Details= detailsRepository.saveAndFlush(customer_Details);

		addressServices.createModifyCustAddressDetails(addressDetailsDto,customer_Details);
		//Documents Details Creation
		doc.setCustomer(customer_Details);
		docRepository.saveAndFlush(doc);
		//create Nominee details
		nomineeService.createNomineesDetails(nomineeDtoList,customer_Details,1);
	}

	@Cacheable(value = "allCustomersCache", key = "'allCustomers'")
	@Override
	public List<CustomerDto> getAllCust(){
		List<CustomerDetails> allcust = detailsRepository.findAll();
		return allcust.stream()
				.map(c -> CustomerDetailsMapper.mapToCustomerDetailsDto(c, new CustomerDto()))
				.collect(Collectors.toList());
	}

	@Override
	public List<ResponseWrapperDto> searchCustomers(CustomerSearchRequestDTO requestDTO) {

		Specification<CustomerDetails> spec = Specification.where(null);

		if (requestDTO.getCustomerId() != null && !requestDTO.getCustomerId().isEmpty()) {
			spec = spec.and(CustomerDetailsSpecification.hasCustomerId(requestDTO.getCustomerId()));
		}

		if (requestDTO.getName() != null && !requestDTO.getName().isEmpty()) {
			spec = spec.and(CustomerDetailsSpecification.hasName(requestDTO.getName()));
		}

		if (requestDTO.getStatus() != null && !requestDTO.getStatus().isEmpty()) {
			spec = spec.and(CustomerDetailsSpecification.hasStatus(requestDTO.getStatus()));
		}

		if (requestDTO.getStartDate() != null && requestDTO.getEndDate() != null) {
			spec = spec.and(CustomerDetailsSpecification.isBetweenDates(requestDTO.getStartDate(), requestDTO.getEndDate()));
		}

		List<CustomerDetails> customerDetailsList = detailsRepository.findAll(spec);
		List<ResponseWrapperDto> responseList = new ArrayList<>();

		for(CustomerDetails customer :  customerDetailsList){
			ResponseWrapperDto response = new ResponseWrapperDto();
			CustomerKey key = customer.getCustomerId();
			String customerId = key.getCustomerId();
			int customerType= key.getCustomerType();
			CustomerAddressDto addressDto = addressServices.findAddressByCustomerID(customerId,customerType);
			DocumentsDtlsDto documentsDto = documentsServices.getDocumentsByCustomerId(key);
			List<NomineeDto> nomineeDtoList = nomineeService.findNomineeByCustomerId(key);
			CustomerDto customerDto = CustomerDetailsMapper.mapToCustomerDetailsDto(customer, new CustomerDto());
			response.setCustomerDetails(customerDto);
			response.setAddressDetails(addressDto);
			response.setDocumentDetails(documentsDto);
			response.setNomineeDetails(nomineeDtoList);
			responseList.add(response);

		}
		return responseList;
	}

	public ResponseWrapperDto searchSingleCustomerData(String customerId, String  customerType) {
		CustomerKey key = new CustomerKey();
		key.setCustomerId(customerId);
		key.setCustomerType(Integer.parseInt(customerType));

		Optional<CustomerDetails> customerDetails = detailsRepository.findById(key);

		if(customerDetails.isPresent()) {
			ResponseWrapperDto response = new ResponseWrapperDto();
			CustomerAddressDto addressDto = addressServices.findAddressByCustomerID(customerId, Integer.parseInt(customerType));
			DocumentsDtlsDto documentsDto = documentsServices.getDocumentsByCustomerId(key);
			List<NomineeDto> nomineeDtoList = nomineeService.findNomineeByCustomerId(key);
			CustomerDto customerDto = CustomerDetailsMapper.mapToCustomerDetailsDto(customerDetails.get(), new CustomerDto());
			response.setCustomerDetails(customerDto);
			response.setAddressDetails(addressDto);
			response.setDocumentDetails(documentsDto);
			response.setNomineeDetails(nomineeDtoList);
			return response;
		}
		return new ResponseWrapperDto();

	}

	@Cacheable(value = "customers", key = "#mobileNumber")
	@Override
	public CustomerDto findCustomerByMobileNumber(String mobileNumber) {
		Optional<CustomerDetails> customerDetails = detailsRepository.findByMobileNumber(mobileNumber);
		if(customerDetails.isEmpty()){
			throw new ResourceNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND + " Customer MobileNumber :"+ mobileNumber);
		}
        return CustomerDetailsMapper.mapToCustomerDetailsDto(customerDetails.get(), new CustomerDto());
	}

	@CachePut(value = "customers", key = "#customerInp.mobileNumber")
	@Override
	public CustomerDto modifyCustomer(CustomerDto customerInp, String customerId,int customerType) {
		CustomerKey customerKey = new CustomerKey(customerId,customerType);
		CustomerDto cDto = new CustomerDto();
		CustomerDetails customer;
		customer = detailsRepository.findByCustomerId(customerKey).orElseThrow(
				()-> new CustomerNotFoundException(new ErrorResponse(null, HttpStatus.NOT_FOUND,
						"Customer Not Found with Key :"+customerKey, LocalDateTime.now()))
		);
		Optional<CustomerDetails> customerDetails = detailsRepository.findById(customerKey);
		if(customerDetails.isPresent()) {
            CustomerDetailsMapper.mapToCustomerDetails(customerInp, customer);
            customer = detailsRepository.save(customer);
			cDto = CustomerDetailsMapper.mapToCustomerDetailsDto(customer, new CustomerDto());
		}
		return cDto;
	}
}
