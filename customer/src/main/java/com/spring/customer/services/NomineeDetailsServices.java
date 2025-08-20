package com.spring.customer.services;


import com.spring.customer.customer.CustomerDetails;
import com.spring.customer.customer.CustomerKey;
import com.spring.customer.customer.NomineeDetails;
import com.spring.customer.dto.NomineeDto;
import com.spring.customer.error.ResourceNotFoundException;
import com.spring.customer.mapper.NomineeMapper;
import com.spring.customer.repository.Nominee_Repository;
import com.spring.customer.services.si.NomineeServiceInterface;
import com.spring.customer.utils.SequenceGenerator;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NomineeDetailsServices implements NomineeServiceInterface {
	private static final Logger log = LogManager.getLogger(NomineeDetailsServices.class);

    private final Nominee_Repository nomineeRepository;
	private final SequenceGenerator sequenceGenerator;

    @Override
	public List<NomineeDto> createNomineesDetails(List<NomineeDto> nomineeDetails, CustomerDetails customerDetails, int flag)
	{
		NomineeDetails nominee_details;
		List<NomineeDetails> nominee_detail_out = new ArrayList<>();
		for(NomineeDto nominee : nomineeDetails) {
			NomineeDetails nominee_details_temp;
			nominee_details = NomineeMapper.mapToNominee(nominee, new NomineeDetails());
			BigInteger nomineeId = sequenceGenerator.generateSequence("NomineeId_seq");
			nominee_details.setNomineeId(nomineeId.intValue());
			nominee_details.setCustomer(customerDetails);
			nominee_details_temp=nomineeRepository.save(nominee_details);
			nominee_detail_out.add(nominee_details_temp);
		}
		List<NomineeDto> nomineeDtos = new ArrayList<>();
		for(NomineeDetails nomineeDetails1 : nominee_detail_out){
			nomineeDtos.add(NomineeMapper.mpToNomineeDto(nomineeDetails1,new NomineeDto()));
		}
		return nomineeDtos;
	}

	@Override
	public List<NomineeDto> modifyNomineeDetails(List<NomineeDto> nomineeDetails,int flag){
		NomineeDetails nominee_details;
		List<NomineeDetails> nominee_detail_arr = new ArrayList<>();
		if(!nomineeDetails.isEmpty()) {
			for(NomineeDto nominee : nomineeDetails) {
                new NomineeDetails();
                NomineeDetails nominee_details_temp;
				Optional<NomineeDetails> nomineeOut = nomineeRepository.findByNomineeRefNum(nominee.getNomineeRefNum());
				if(nomineeOut.isPresent()) {
					nominee_details = NomineeMapper.mapToNominee(nominee, new NomineeDetails());
					nominee_details.setNomineeId(nomineeOut.get().getNomineeId());
					nominee_details_temp=nomineeRepository.save(nominee_details);
					
					nominee_detail_arr.add(nominee_details_temp);

				}else {
					log.info("There is No Details Present");
				}
			}
		}
		List<NomineeDto> nomineeDtos = new ArrayList<>();
		for(NomineeDetails nomineeDetails1 : nominee_detail_arr){
			nomineeDtos.add(NomineeMapper.mpToNomineeDto(nomineeDetails1,new NomineeDto()));
		}

		return nomineeDtos;
    }
	
    @Override
	public void deleteNominee(NomineeDto nominee_Details) {
		int nomineeId= nominee_Details.getNomAddId();
		if(nomineeRepository.findById(nomineeId).isPresent()) {
			nomineeRepository.deleteById(nomineeId);
		}else {
			throw new ResourceNotFoundException("Nominee Not Existing with given Id"+ nomineeId);
		}
		
	}

    @Override
	public List<NomineeDto> findNomineeByOwnerId(int ownerId) {
		List<NomineeDetails> nominee_detail_arr;
		List<NomineeDto> nomineeDtos = new ArrayList<>();
		nominee_detail_arr = nomineeRepository.findByOwnerId(ownerId);
		
		if(nominee_detail_arr.isEmpty()) {
			throw new ResourceNotFoundException("Nominee Not Found");
		}else {
			for(NomineeDetails nomineeDetails: nominee_detail_arr) {
                nomineeDtos.add(NomineeMapper.mpToNomineeDto(nomineeDetails, new NomineeDto()));
            }
			return nomineeDtos;
		}
		
	}


	public List<NomineeDto> findNomineeByCustomerId(CustomerKey key) {
		List<NomineeDetails> nomineeDetailsList = nomineeRepository.findByCustomer_CustomerId_CustomerIdAndCustomer_CustomerId_CustomerType(key.getCustomerId(),key.getCustomerType());
		return nomineeDetailsList.stream()
				.map(nominee -> NomineeMapper.mpToNomineeDto(nominee, new NomineeDto()))
				.toList();

	}
}
