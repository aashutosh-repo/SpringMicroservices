package com.spring.core.repository;

import com.spring.core.entity.TokenVault;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenizationRepository extends JpaRepository<TokenVault, Long> {
    Optional<TokenVault> findByToken(String token);
}
