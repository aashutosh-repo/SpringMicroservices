package com.spring.core.repository;

import com.spring.core.entity.PopulationCensusIndia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndiaPopulationRepository extends JpaRepository<PopulationCensusIndia,Long> {

}
