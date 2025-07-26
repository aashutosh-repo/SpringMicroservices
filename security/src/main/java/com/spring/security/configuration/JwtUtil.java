package com.spring.security.configuration;

import com.spring.security.services.KeyLoader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
@Data
public class JwtUtil {

    private final KeyPair keyPair;
//    private final PrivateKey privateKey;
//    private final PublicKey publicKey;

    public JwtUtil() {
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            this.keyPair = keyPairGenerator.generateKeyPair();
//            this.privateKey = KeyLoader.loadPrivateKey("keys/private_key.pem");
//            this.publicKey = KeyLoader.loadPublicKey("keys/public_key.pem");
        }catch (Exception e){
            throw new RuntimeException("RSA Key Generation Failed", e);
        }
    }

    public String generateToken(String userName, List<String> roles){

        return Jwts.builder()
                .setSubject(userName)
                .claim("role", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+3600000))
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    public Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(keyPair.getPublic())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public PublicKey getPublicKey(){
        return keyPair.getPublic();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public boolean validateToken(String token, String username) {
        final String tokenUsername = extractUsername(token);
        return (tokenUsername.equals(username) && !isTokenExpired(token));
    }

    private PrivateKey loadPrivateKey(String filePath) throws Exception {
        String key = readPemFile(filePath)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    private PublicKey loadPublicKey(String filePath) throws Exception {
        String key = readPemFile(filePath)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private String readPemFile(String filePath) throws Exception {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) throw new IllegalArgumentException("File not found: " + filePath);
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
