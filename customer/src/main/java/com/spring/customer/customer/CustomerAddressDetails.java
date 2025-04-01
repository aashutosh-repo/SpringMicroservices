package com.spring.customer.customer;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CustomerAddressDetails {
	@EmbeddedId
	private AddressID addressId;
	private int addressType;
	private String addressLn1;
	private String addressLn2;
	private String city;
	private String village;
	private String district;
	private String taluka;
	private String state;
	private int pinCode;
	private LocalDate lastUpdate;
	private Date dateOfCapture;
}
