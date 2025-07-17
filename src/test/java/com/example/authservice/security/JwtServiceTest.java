package com.example.authservice.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String secret = "mi-super-clave-secreta-segura-y-larga-para-test-so-larali.larala-agregandomassecretos";
    private Key signingKey;

    private String username = "osvaldo";
    private String role = "ROLE_ADMIN";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        jwtService.secretKey = secret; // simula el @Value
        signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private String createToken(String username, String role, Date expirationDate) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .claim("role", role)
                .signWith(signingKey, SignatureAlgorithm.HS512)
                .compact();
    }

    @Test
    void shouldExtractUsernameAndRoleCorrectly() {
        String token = createToken(username, role, new Date(System.currentTimeMillis() + 100_000));

        assertEquals(username, jwtService.extractUsername(token));
        assertEquals(role, jwtService.extractRol(token));
    }

    @Test
    void shouldValidateTokenWithMatchingUserDetails() {
        String token = createToken(username, role, new Date(System.currentTimeMillis() + 100_000));

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void shouldRejectTokenWithWrongUsername() {
        String token = createToken(username, role, new Date(System.currentTimeMillis() + 100_000));

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("otroUsuario");

        assertFalse(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void shouldDetectExpiredToken() {
        Date expiredDate = new Date(System.currentTimeMillis() - 86400000); // hace 1 día
        String token = createToken(username, role, expiredDate);

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        assertThrows(ExpiredJwtException.class, () -> {
            jwtService.isTokenValid(token, userDetails);
        });
    }

}
