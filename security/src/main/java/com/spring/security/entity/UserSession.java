package com.spring.security.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_sessions")
@Data
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false, unique = true,length = 2048)
    private String jwtToken;

    @Column(nullable = false)
    private LocalDateTime loginTime;

    @Column(nullable = false)
    private LocalDateTime lastActivity;

    private String ipAddress;
    private String deviceInfo;

    @Column(nullable = false)
    private Boolean isActive = true;

    // Getters and Setters
}
