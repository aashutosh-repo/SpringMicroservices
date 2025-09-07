package com.spring.batch.repositories.payment;

import com.spring.batch.paymentEntity.FileMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata,Long> {
    Page<FileMetadata> findByFileType(String fileType, Pageable pageable);
}
