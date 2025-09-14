package com.bancs.payments.repository;

import com.bancs.payments.entity.Sequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SequenceRepository extends JpaRepository<Sequence, String> {

    Sequence findBySequenceName(String sequenceName);
    @Query(value = "SELECT MAX(seq_id) FROM Sequences", nativeQuery = true)
    Optional<Integer> findMaxSequenceId();
}
