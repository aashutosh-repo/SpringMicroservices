package com.spring.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.validator.constraints.ISBN;

@Entity
@Data
public class IndianCities {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cityId;
    private String city;
    private String lat;
    private String lon;
    private String country;
    private String iso2;
    private String state;
}
