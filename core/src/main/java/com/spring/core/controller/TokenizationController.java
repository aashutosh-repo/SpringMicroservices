package com.spring.core.controller;

import com.spring.core.services.DataSecurityService;
import com.spring.core.services.TokenizationService;
import com.spring.core.utils.CardMaskUtil;
import com.spring.core.utils.EncryptionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.TypeCollector;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class TokenizationController {

    private static final String PAYLOAD= "payload";
    @Autowired
    private TokenizationService tokenizationService;
    @Autowired
    private DataSecurityService securityService;

    @PostMapping("/secureCard/tokenize")
    public String tokenizeCard(@RequestParam String cardNumber) throws Exception {
        return tokenizationService.tokenizeCard(cardNumber);
    }

    @GetMapping("/secureCard/detokenize")
    public String detokenize(@RequestParam String token) throws Exception {
        return tokenizationService.detokenize(token);
    }

    @GetMapping("/cardMasking")
    public ResponseEntity<String> cardMasking(@RequestBody String cardNumber){
        String maskedCard= CardMaskUtil.maskCardNumber(cardNumber);
        return ResponseEntity.ok(maskedCard);
    }


    @PostMapping(value = "/encrypt",  consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> encryptData(@RequestBody Map<String, String> payload) {
        try {
            String encryptedPayload = payload.get(PAYLOAD);
            String decryptedData = securityService.encrypt(encryptedPayload);
            return ResponseEntity.ok(decryptedData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Decryption failed: " + e.getMessage());
        }
    }
    @PostMapping(value = "/decrypt", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> decryptData(@RequestBody Map<String, String> requestData) {
        try {
            String encryptedPayload = requestData.get(PAYLOAD);
            String decryptedData = securityService.decrypt(encryptedPayload);
            return ResponseEntity.ok(decryptedData);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Decryption failed: " + e.getMessage());
        }
    }
}
