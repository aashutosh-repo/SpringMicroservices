package com.spring.customer.services.si;


import com.spring.customer.customer.CustomerDetails;
import com.spring.customer.customer.NomineeDetails;
import com.spring.customer.dto.NomineeDto;

import java.util.List;

public interface NomineeServiceInterface {
	List<NomineeDto> createNomineesDetails(List<NomineeDto> nomineeDetails, CustomerDetails customerDetails, int flag);
	List<NomineeDto> modifyNomineeDetails(List<NomineeDto> nominee,int flag);
	void deleteNominee(NomineeDto nominee_Details);
	List<NomineeDto> findNomineeByOwnerId(int ownerId);
}
