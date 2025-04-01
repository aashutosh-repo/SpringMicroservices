package com.spring.core.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;

@Entity
public class TokenVault {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    @Column(name = "encrypted_card_data")
    private String encryptedCardData;
    private String token;
    private SecretKey secureKey;
    private LocalDateTime creationTime;

    public Long getTokenId() {
        return tokenId;
    }

    public void setTokenId(Long tokenId) {
        this.tokenId = tokenId;
    }

    public String getEncryptedCardData() {
        return encryptedCardData;
    }

    public void setEncryptedCardData(String encryptedCardData) {
        this.encryptedCardData = encryptedCardData;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public SecretKey getSecureKey() {
        return secureKey;
    }

    public void setSecureKey(SecretKey secureKey) {
        this.secureKey = secureKey;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }
}
