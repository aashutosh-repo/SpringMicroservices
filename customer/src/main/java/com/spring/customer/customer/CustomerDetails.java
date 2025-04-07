package com.spring.customer.customer;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CustomerDetails {
	@EmbeddedId
	private CustomerID customerId; //PK
	private String customerCategory;
	private String firstName;
	private String lastName;
	private String fatherName;
	private String motherName;
	private String email;
	private String mobileNumber;
	private int status;
	private int addressId;
	private int nomineeId;
	private LocalDate dateOfBirth;
	private LocalDate custCreationDt;
	private LocalDate custClsngDt;
	private int riskProfile;
	private LocalDate lastUpdate;
	private String ratingAgency;
}
