package com.spring.security.repository;

import com.spring.security.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserSessionRepository extends JpaRepository<UserSession, UUID> {
    Optional<UserSession> findByJwtToken(String jwtToken);
}

