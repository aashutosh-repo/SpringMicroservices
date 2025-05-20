package com.spring.customer.customer;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDate;

@Entity
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public class NomineeDetails {
	@Id
	protected int nomineeId; //pk
	private int nomineeRefNum; //for external Use
	private int ownerId;
    private int ownerType;
    private int seqNum;
    private int nomShare;
    private int nomType;
    private int nomTypeCode;
    private String nomineeFirstName;
    private String nomineeMiddleName;
    private String nomineeLastName;
    private int rtlnType;
    private int rtlnTypeCode;
    private LocalDate dateOfBirth;
    private int nomAddId;
    private String nomDocId;
    private int ver;
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "customerId", referencedColumnName = "customerId"),
            @JoinColumn(name = "customerType", referencedColumnName = "customerType")
    })
    private CustomerDetails customer;
}
