package com.example.authservice.security;

import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    private final String secret = "mi-super-clave-secreta-segura-y-larga-para-test-so-larali.larala-agregandomassecretos";

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        jwtUtil.secretKey = secret;
        jwtUtil.init();             // simulamos @PostConstruct
    }

    @Test
    void shouldGenerateAndValidateTokenCorrectly() {
        String username = "osvaldo";
        String role = "ROLE_ADMIN";

        String token = jwtUtil.generateToken(username, role);

        assertNotNull(token);
        assertTrue(jwtUtil.isTokenValid(token, username));
        assertEquals(username, jwtUtil.extractUsername(token));
        assertEquals(role, jwtUtil.extractRole(token));
        assertFalse(jwtUtil.extractExpiration(token).before(new Date())); // no debe estar expirado
    }

    @Test
    void shouldInvalidateTokenWithWrongUsername() {
        String token = jwtUtil.generateToken("marcus", "ROLE_USER");
        assertFalse(jwtUtil.isTokenValid(token, "isismami"));
    }

    @Test
    void shouldDetectExpiredToken() {
        jwtUtil.setExpirationTime(-1000); // ya expirado
        String token = jwtUtil.generateToken("foo", "ROLE_USER");
        assertFalse(jwtUtil.isTokenValid(token, "foo"));
    }

    @Test
    void shouldExtractAllClaims() {
        String token = jwtUtil.generateToken("foo", "ROLE_TEST");

        String username = jwtUtil.extractUsername(token);
        String role = jwtUtil.extractRole(token);
        Date expiration = jwtUtil.extractExpiration(token);

        assertEquals("foo", username);
        assertEquals("ROLE_TEST", role);
        assertNotNull(expiration);
    }
}