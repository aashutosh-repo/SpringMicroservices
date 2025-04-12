package com.spring.batch.demoentity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class IndiaPincodes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String circleName;
    private String regionName;
    private String divisionName;
    private String officeName;
    private String pincode;
    private String officeType;
    private String delivery;
    private String district;
    private String stateName;
    private String latitude;
    private String longitude;
}
