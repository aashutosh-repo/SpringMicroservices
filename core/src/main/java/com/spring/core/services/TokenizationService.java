package com.spring.core.services;

import com.spring.core.entity.TokenVault;
import com.spring.core.repository.TokenizationRepository;
import com.spring.core.services.si.TokenInterface;
import com.spring.core.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenizationService implements TokenInterface {

    @Autowired
    private TokenizationRepository repository;

    @Override
    public String tokenizeCard(String cardNumber) {
        SecretKey secretKey = EncryptionUtil.generateKey();
        String encryptedData = EncryptionUtil.encrypt(cardNumber,secretKey);
        String token = UUID.randomUUID().toString().replace("-", "");

        TokenVault tokenVault = new TokenVault();
        tokenVault.setToken(token);
        tokenVault.setEncryptedCardData(encryptedData);
        tokenVault.setSecureKey(secretKey);
        tokenVault.setCreationTime(LocalDateTime.now());

        repository.save(tokenVault);
        return token;
    }

    @Override
    public String detokenize(String token) {
        TokenVault tokenVault = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        return EncryptionUtil.decrypt(tokenVault.getEncryptedCardData(),tokenVault.getSecureKey());
    }
}
