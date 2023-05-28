package ru.iql.banking.services.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl {

    @Value("${banking.security.jwt.secret-key}")
    private String secretKey;

    private static final String USER_ID = "USER_ID";

    public String extractUserId(String token) {
        Claims claims;
        try{
            claims = extractAllClaims(token);
        }
        catch(Exception e){
            return null;
        }
        return (String)claims.get(USER_ID);

    }

    public String generateToken(String userData){
        Map<String, String> claims = new HashMap<>();
        claims.put(USER_ID, userData);
        return Jwts
                .builder()
                .setClaims(claims)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
