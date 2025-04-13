package com.spring.batch.repositories.demography;

import com.spring.batch.demoentity.PopulationCensusIndia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IndiaPopulationRepository extends JpaRepository<PopulationCensusIndia,Long> {

}
