package com.spring.core.utils;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Blob;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "TempModifiedEntity")
public class TempModifiedEntity {
	@Id
    @Column(name = "modified_key", nullable = false, unique = true)
    private String modifiedKey;

    @Column(name = "status", nullable = false)
    private String status = "PENDING";  // Default value for status

    @Column(name = "entity_name", nullable = false)
    private String entityName;

    @Lob
    @Column(name = "modified_data", nullable = false)
    private Blob modifiedData;

    @Column(name = "modified_date", nullable = false)
    private LocalDate modifiedDate;
}