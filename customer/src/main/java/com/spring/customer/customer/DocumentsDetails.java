package com.spring.customer.customer;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class DocumentsDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int docId; // Primary key
	private String docDescription;
	private String docIdentificationNumber;
	private String docType;
	private String DocTypeCode;
	private LocalDate issueDate;
	private LocalDate expiryDate;
	@OneToOne
	@JoinColumns({
			@JoinColumn(name = "customerId", referencedColumnName = "customerId"),
			@JoinColumn(name = "customerType", referencedColumnName = "customerType")
	})
	private CustomerDetails customer;
}
