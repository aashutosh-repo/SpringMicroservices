package com.spring.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtUtil {
    private static final String SECRET_KEY =  "4Ldrf8R1wK6m9Xb2Tz7qYvGp3Nh5UfJc"; //need to secure with properties file

    public Claims extractClaims(String token) throws Exception {
        return Jwts.parserBuilder()
                .setSigningKey(KeyLoader.loadPublicKey("keys/public_key.pem"))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isValidToken(String token){
        try{
            extractClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getUserName(String token) throws Exception {
        return  extractClaims(token).getSubject();
    }
}