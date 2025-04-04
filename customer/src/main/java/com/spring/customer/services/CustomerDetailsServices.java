package com.spring.customer.services;


import com.spring.customer.customer.CustomerDetails;
import com.spring.customer.customer.CustomerID;
import com.spring.customer.customer.DocumentsDetails;
import com.spring.customer.customer.NomineeDetails;
import com.spring.customer.dto.CustomerDto;
import com.spring.customer.dto.DocumentsDtlsDto;
import com.spring.customer.dto.NomineeDto;
import com.spring.customer.error.ErrorCode;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
	private final NomineeDetailsServices nomineeService;
	private final SequenceGenerator sequenceGenerator;

	@Override
	@CacheEvict(value = "customers", key = "'allCustomers'", allEntries = false)
	public void CreateCustDetails(CustomerDto inp_cust_details,
								  DocumentsDtlsDto documentsDtlsDto, List<NomineeDto> nomineeDtoList){

        new CustomerDetails();
        CustomerDetails customer_Details;
        new NomineeDetails();
        new DocumentsDetails();
        DocumentsDetails doc;
		//Create customer Details
		customer_Details = CustomerDetailsMapper.mapToCustomerDetails(inp_cust_details, new CustomerDetails());
		customer_Details.setCustClsngDt(null);
		doc = DocumentDetailsMapper.mapToDocumentDetails(documentsDtlsDto, new DocumentsDetails());

		BigInteger entityId = sequenceGenerator.generateSequence("CustomerID_seq");
		BigInteger documentId = sequenceGenerator.generateSequence("DocumentID_seq");
		CustomerID custKey = new CustomerID();
		custKey.setCustomerID(entityId.intValue());
		custKey.setCustomerType(2);
		customer_Details.setCustomerId(custKey);
		customer_Details= detailsRepository.saveAndFlush(customer_Details);

		//Documents Details Creation
		doc.setDocId(documentId.intValue());
		doc.setCustId(customer_Details.getCustomerId().getCustomerID());
//		docRepository.saveAndFlush(doc);
		//create Nominee details
		nomineeService.createNomineesDetails(nomineeDtoList,1);
	}

	@Cacheable(value = "customers", key = "'allCustomers'")
	@Override
	public List<CustomerDto> getAllCust(){
		List<CustomerDetails> allcust = detailsRepository.findAll();
		List<CustomerDto> allCustDtoOut = new ArrayList<>();
		for(CustomerDetails cust : allcust) {
			CustomerDto customerDetailsSingle = CustomerDetailsMapper.mapToCustomerDetailsDto(cust, new CustomerDto());
			allCustDtoOut.add(customerDetailsSingle);
		}

		return allCustDtoOut;
	}

	@Override
	public List<CustomerDto> findCustmerByDates(String customerType, LocalDate startDate, LocalDate endDate) {
		List<CustomerDetails> customerDetailsList= detailsRepository.findByCustomerCategoryAndCustCreationDtBetween(customerType,startDate,endDate);
		List<CustomerDto> customerDtoList = new ArrayList<>();
		for(CustomerDetails c: customerDetailsList){
			customerDtoList.add(CustomerDetailsMapper.mapToCustomerDetailsDto(c, new CustomerDto()));
		}
		return customerDtoList;
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
	public CustomerDto modifyCustomer(CustomerDto customerInp, int customerId,int customerType) {
		CustomerID cusPkey = new CustomerID();
		CustomerDto cDto = new CustomerDto();
		CustomerDetails customer = new CustomerDetails();
		cusPkey.setCustomerID(customerId);
		cusPkey.setCustomerType(customerType);
		Optional<CustomerDetails> customerDetails = detailsRepository.findById(cusPkey);
		if(customerDetails.isPresent()) {
            CustomerDetailsMapper.mapToCustomerDetails(customerInp, customer);
            customer = detailsRepository.save(customer);
			cDto = CustomerDetailsMapper.mapToCustomerDetailsDto(customer, new CustomerDto());
		}
		return cDto;
	}
}
