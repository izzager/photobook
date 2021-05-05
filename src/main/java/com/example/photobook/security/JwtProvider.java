package com.example.photobook.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${photobook.token-issuer}")
    private String issuer;

    @Value("${photobook.token-audience}")
    private String audience;

    public String generateToken(String username) {
        Date date = Date.from(LocalDate.now()
                .plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return Jwts.builder()
                .setIssuer(issuer)
                .setAudience(audience)
                .setSubject(username)
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
            return isCorrectClaims(claims);
        } catch (JwtException | IllegalStateException | NullPointerException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    private boolean isCorrectClaims(Claims claims) {
        return claims.getIssuer().equals(issuer)
                || claims.getAudience().equals(audience);
    }

}
