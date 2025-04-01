package com.spring.core.controller;

import com.spring.core.services.TokenizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
}
