package com.spring.customer.controller;


import com.spring.customer.customer.NomineeDetails;
import com.spring.customer.dto.NomineeDto;
import com.spring.customer.mapper.NomineeMapper;
import com.spring.customer.repository.Nominee_Repository;
import com.spring.customer.services.NomineeDetailsServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.links.Link;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Omega Bank Nominee Controller",
description = "This Swagger containing CRUD operation for Nominee Management")
@RestController
@RequestMapping("/customer/nominee")
@AllArgsConstructor
public class NomineeController {
	private Nominee_Repository nomRepo;
	private NomineeDetailsServices nomineeService;

    @Operation(summary = "Create Nominee API",
    description = "REST API to create Nominee in Omega Bank")
    @ApiResponse(
            responseCode = "201",
            description = "Http Status CREATED",
            links = @Link()
    )    
    @PostMapping("/insert")
    public ResponseEntity<List<NomineeDto>> insertInto(@RequestBody List<NomineeDto> nominee){
    	List<NomineeDto> nominee_Details;
    	nominee_Details=nomineeService.createNomineesDetails(nominee,1);
    	return ResponseEntity.ok(nominee_Details);
    }
    
    @GetMapping("/nomineeById")
    public ResponseEntity<NomineeDto> showNomineeDetails(@RequestParam int nomineeId){
    	Optional<NomineeDetails>  nominee= nomRepo.findById(nomineeId);
    	if(nominee.isPresent()){
			NomineeDto dto = NomineeMapper.mpToNomineeDto(nominee.get(), new NomineeDto());
			return ResponseEntity.ok(dto);
    	}
		return ResponseEntity.ok(null);
	}
    
    @GetMapping("/nomineeByOwnerId") 
    public ResponseEntity<List<NomineeDto>> showNomineeDetailsByOwnerId(@RequestParam int ownerId){
    	List<NomineeDto>  nominees= nomineeService.findNomineeByOwnerId(ownerId);
    	return ResponseEntity.ok(nominees);
    }


}
