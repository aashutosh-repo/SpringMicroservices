package com.spring.batch.repositories.demography;


import com.spring.batch.demoentity.IndiaPincodes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IndiaPincodesRepository extends JpaRepository<IndiaPincodes, Long> {
}
