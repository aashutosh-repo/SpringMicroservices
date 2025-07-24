package com.spring.core.services;

import com.spring.core.utils.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:securityKey.properties")
@RequiredArgsConstructor
public class DataSecurityService {

    public String decrypt(String encryptedPayload){
        return EncryptionUtil.decrypt(encryptedPayload);
    }
    public String encrypt(String jsonPayload){
        return EncryptionUtil.encrypt(jsonPayload);
    }
}
