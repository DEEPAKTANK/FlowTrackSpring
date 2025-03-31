package com.deepak.proexpenditure.pro_expenditure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret}") // Load secret from properties
    private String secretKey;

    @Value("${jwt.expiration}") // Expiration time in ms
    private long expirationTime;

    /**
     * Generates a JWT token for the given user ID.
     */
    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts the user ID from the given JWT token.
     */
    public String extractUserId(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Validates the given JWT token.
     */
    public boolean validateToken(String token, String userId) {
        try {
            return extractUserId(token).equals(userId) && !isTokenExpired(token);
        } catch (JwtException e) {
            log.warn("JWT validation error: {}", e.getMessage());
            return false;
        }
    }

    public ResponseEntity<String> checkTokenExpiration(String token) {
        try {
            boolean expired = isTokenExpired(token);
            if (expired) {
                return ResponseEntity.status(401).body("Token has expired");
            }
            return ResponseEntity.ok("Token is valid");
        } catch (ExpiredJwtException e) {
            return ResponseEntity.status(401).body("Token has expired");
        } catch (UnsupportedJwtException e) {
            return ResponseEntity.status(400).body("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            return ResponseEntity.status(400).body("Malformed JWT token");
        } catch (SignatureException e) {
            return ResponseEntity.status(403).body("Invalid JWT signature");
        } catch (JwtException e) {
            return ResponseEntity.status(400).body("Invalid JWT token");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            return extractClaim(token, Claims::getExpiration).before(new Date());
        } catch (JwtException e) {
            log.warn("Error checking token expiration: {}", e.getMessage());
            return true; // Assume expired in case of error
        }
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claimsResolver.apply(claims);
        } catch (ExpiredJwtException e) {
            log.warn("JWT token has expired: {}", e.getMessage());
            throw new JwtException("JWT token has expired");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT token: {}", e.getMessage());
            throw new JwtException("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT token: {}", e.getMessage());
            throw new JwtException("Malformed JWT token");
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
            throw new JwtException("Invalid JWT signature");
        } catch (JwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            throw new JwtException("Invalid JWT token");
        }
    }

    private Key signingKey;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    private Key getSigningKey() {
        return signingKey;
    }
}
