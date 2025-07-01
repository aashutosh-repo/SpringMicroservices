package com.spring.customer.customer;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data @EqualsAndHashCode
@AllArgsConstructor @NoArgsConstructor
public class CustomerKey  implements Serializable{
	@Column(name = "customerId")
	private String customerId;

	@Column(name = "customerType")
	private int customerType;
}

