package com.spring.security.controller;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import com.spring.security.configuration.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Map;

@RestController
@RequestMapping("/auth/security/.well-known")
@RequiredArgsConstructor
public class SecurityController {

    private final JwtUtil jwtUtil;


    @GetMapping("/jwks.json")
    public Map<String, Object> exposeJwk() {
        RSAPublicKey publicKey = (RSAPublicKey) jwtUtil.getPublicKey();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .keyUse(KeyUse.SIGNATURE)
                .algorithm(JWSAlgorithm.RS256)
                .keyID("my-key-id")
                .build();
        return new JWKSet(rsaKey).toJSONObject();
    }
    @GetMapping("/public-key")
    public ResponseEntity<String> getPublicKey() {
        PublicKey publicKey = jwtUtil.getPublicKey();
        String encoded = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        String pem = "-----BEGIN PUBLIC KEY-----\n" +
                encoded.replaceAll("(.{64})", "$1\n") +
                "\n-----END PUBLIC KEY-----";

        return ResponseEntity.ok(pem);
    }
}
