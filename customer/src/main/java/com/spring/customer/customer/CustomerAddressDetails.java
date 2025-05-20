package com.spring.customer.customer;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data @RequiredArgsConstructor
public class CustomerAddressDetails {
	@Id
	private Long addressId;
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
	@OneToOne
	@JoinColumns({
			@JoinColumn(name = "customerId", referencedColumnName = "customerId"),
			@JoinColumn(name = "customerType", referencedColumnName = "customerType")
	})
	private CustomerDetails customer;
}
