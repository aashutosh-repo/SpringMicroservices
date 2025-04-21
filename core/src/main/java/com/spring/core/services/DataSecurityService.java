package com.spring.core.services;

import com.spring.core.utils.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;

@Service
@PropertySource("classpath:securityKey.properties")
@RequiredArgsConstructor
public class DataSecurityService {
    @Value("${spring.secure.key}")
    private String secretKey;

    public String decrypt(String encryptedPayload){
        SecretKey mySecretKey = EncryptionUtil.getSecretKeyFromString(secretKey);
        return EncryptionUtil.decrypt(encryptedPayload, mySecretKey);
    }
    public String encrypt(String jsonPayload){
        SecretKey mySecretKey = EncryptionUtil.getSecretKeyFromString(secretKey);
        return EncryptionUtil.decrypt(jsonPayload, mySecretKey);
    }
}
