package com.unir.socialhabits.security;

import com.unir.socialhabits.entities.Professional;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // ⚠️ Cambia esto en producción
    private static final String SECRET_KEY =
            "mysecretkeymysecretkeymysecretkey12345";

    private static final long EXPIRATION_TIME =
            1000 * 60 * 60 * 24; // 24h

    private Key getSigningKey() {

        return Keys.hmacShaKeyFor(
                SECRET_KEY.getBytes()
        );
    }

    // GENERAR TOKEN
    public String generateToken(Professional professional) {

        return Jwts.builder()

                .setSubject(professional.getEmail())

                .claim("role", professional.getRole())

                .claim("name", professional.getName())

                .setIssuedAt(new Date())

                .setExpiration(
                        new Date(
                                System.currentTimeMillis()
                                        + EXPIRATION_TIME
                        )
                )

                .signWith(
                        getSigningKey(),
                        SignatureAlgorithm.HS256
                )

                .compact();
    }

    // EXTRAER EMAIL
    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }

    // VALIDAR TOKEN
    public boolean isTokenValid(String token) {

        try {

            extractAllClaims(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }

    // EXTRAER CLAIMS
    private Claims extractAllClaims(String token) {

        return Jwts.parserBuilder()

                .setSigningKey(getSigningKey())

                .build()

                .parseClaimsJws(token)

                .getBody();
    }
}