package com.deepak.proexpenditure.pro_expenditure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
        } catch (ExpiredJwtException e) {
            log.warn("JWT expired: {}", e.getMessage());
            throw new ExpiredJwtException(null, null, "JWT token has expired");
        } catch (UnsupportedJwtException e) {
            log.warn("Unsupported JWT: {}", e.getMessage());
            throw new JwtException("Unsupported JWT token");
        } catch (MalformedJwtException e) {
            log.warn("Malformed JWT: {}", e.getMessage());
            throw new JwtException("Malformed JWT token");
        } catch (SignatureException e) {
            log.warn("Invalid JWT signature: {}", e.getMessage());
            throw new JwtException("Invalid JWT signature");
        } catch (JwtException e) {
            log.warn("Invalid JWT: {}", e.getMessage());
            throw new JwtException("Invalid JWT token");
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
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
