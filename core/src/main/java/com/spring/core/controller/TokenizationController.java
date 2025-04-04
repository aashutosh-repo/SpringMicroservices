package com.spring.core.controller;

import com.spring.core.services.TokenizationService;
import com.spring.core.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import java.util.Map;

@RestController
@RequestMapping("/core/secureCard")
public class TokenizationController {

    @Autowired
    private TokenizationService tokenizationService;

    @PostMapping("/tokenize")
    public String tokenizeCard(@RequestParam String cardNumber) throws Exception {
        return tokenizationService.tokenizeCard(cardNumber);
    }

    @GetMapping("/detokenize")
    public String detokenize(@RequestParam String token) throws Exception {
        return tokenizationService.detokenize(token);
    }
    private final SecretKey secretKey = EncryptionUtil.generateKey(); // Secure key retrieval


    @PostMapping("/decrypt")
    public ResponseEntity<String> decryptData(@RequestBody Map<String, String> requestData) {
        try {
            String encryptedPayload = requestData.get("encryptedPayload");
            String decryptedData = EncryptionUtil.decrypt(encryptedPayload, secretKey);
            return ResponseEntity.ok(decryptedData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Decryption failed: " + e.getMessage());
        }
    }
}
