package com.spring.customer.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CustomerAddressDto{
	private int customerID;
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
