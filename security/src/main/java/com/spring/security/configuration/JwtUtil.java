package com.spring.security.configuration;

import com.spring.security.services.KeyLoader;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.security.*;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
@Data
public class JwtUtil {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public JwtUtil() {
        try{
            this.privateKey = KeyLoader.loadPrivateKey("keys/private_key.pem");
            this.publicKey = KeyLoader.loadPublicKey("keys/public_key.pem");
        }catch (Exception e){
            throw new RuntimeException("RSA Key Generation Failed", e);
        }
    }

    public String generateToken(String userName, List<String> roles){
        return Jwts.builder()
                .setSubject(userName)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+3600000))
                .signWith(privateKey, SignatureAlgorithm.RS256)
                .compact();
    }

    public Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
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
}
