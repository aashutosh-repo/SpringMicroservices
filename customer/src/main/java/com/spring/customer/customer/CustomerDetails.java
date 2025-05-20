package com.spring.customer.customer;

import com.spring.customer.audit.AuditInfo;
import com.spring.customer.dto.NomineeDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CustomerDetails extends AuditInfo {
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "customerID", column = @Column(name = "customerID")),
			@AttributeOverride(name = "customerType", column = @Column(name = "customerType"))
	})
	private CustomerKey customerId; //PK
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
	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
	private CustomerAddressDetails address;

	@OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
	private DocumentsDetails document;

	@OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
	private List<NomineeDetails> nominees;
}
