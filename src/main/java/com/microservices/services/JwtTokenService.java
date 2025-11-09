package com.microservices.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import java.util.Date;


@Service
public class JwtTokenService {
    private final String SECRET_KEY = "mySecretKey123456789012345678901234567890123456789012345678901234567890"; // Must be 32+ chars

    public String generateAccessToken(String clientId, String username,String scope) {
        return Jwts.builder()
                .setSubject(username)
                .claim("user_name",username)
                .claim("client_id", clientId)
                .claim("scope", scope)
                .setIssuer("Jaugernaut-AuthServer-issuer")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getClientIdFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("client_id", String.class);
    }

}
