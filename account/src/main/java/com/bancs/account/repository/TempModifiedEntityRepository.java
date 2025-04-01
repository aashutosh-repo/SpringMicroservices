package com.bancs.account.repository;

import com.bancs.account.entity.TempModifiedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempModifiedEntityRepository extends JpaRepository<TempModifiedEntity, String> {

	TempModifiedEntity findByModifiedKeyAndEntityName(String modifiedKey, String entityName);
}
