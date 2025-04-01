package com.spring.customer.customer;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter @EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
public class CustomerID  implements Serializable{
	private int customerID;
	private int customerType;
}

