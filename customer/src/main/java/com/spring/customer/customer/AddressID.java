package com.spring.customer.customer;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter @Setter @AllArgsConstructor @NoArgsConstructor @ToString
public  class AddressID implements Serializable {
    protected int customerId;
    protected int addressId;
}
