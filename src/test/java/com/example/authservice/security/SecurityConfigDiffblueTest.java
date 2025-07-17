package com.example.authservice.security;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {SecurityConfig.class, AuthenticationConfiguration.class})
@ExtendWith(SpringExtension.class)
class SecurityConfigDiffblueTest {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private SecurityConfig securityConfig;

    /**
     * Test {@link SecurityConfig#passwordEncoder()}.
     *
     * <ul>
     *   <li>Given {@link SecurityConfig}.
     * </ul>
     *
     * <p>Method under test: {@link SecurityConfig#passwordEncoder()}
     */
    @Test
    @DisplayName("Test passwordEncoder(); given SecurityConfig")
    void testPasswordEncoder_givenSecurityConfig() {
        // Arrange, Act and Assert
        assertTrue(securityConfig.passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    /**
     * Test {@link SecurityConfig#passwordEncoder()}.
     *
     * <ul>
     *   <li>Given {@link SecurityConfig} (default constructor).
     * </ul>
     *
     * <p>Method under test: {@link SecurityConfig#passwordEncoder()}
     */
    @Test
    @DisplayName("Test passwordEncoder(); given SecurityConfig (default constructor)")
    void testPasswordEncoder_givenSecurityConfig2() {
        // Arrange, Act and Assert
        assertTrue(new SecurityConfig().passwordEncoder() instanceof BCryptPasswordEncoder);
    }

    /**
     * Test {@link SecurityConfig#securityFilterChain(HttpSecurity)}.
     *
     * <p>Method under test: {@link SecurityConfig#securityFilterChain(HttpSecurity)}
     */
    @Test
    @DisplayName("Test securityFilterChain(HttpSecurity)")
    @Disabled("TODO: Complete this test")
    void testSecurityFilterChain() throws Exception {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Reason: No inputs found that don't throw a trivial exception.
        //   Diffblue Cover tried to run the arrange/act section, but the method under
        //   test threw
        //   java.lang.IllegalArgumentException: objectPostProcessor cannot be null
        //   See https://diff.blue/R013 to resolve this issue.

        // Arrange
        AuthenticationManagerBuilder authenticationBuilder =
                new AuthenticationManagerBuilder((ObjectPostProcessor<Object>) null);

        // Act
        securityConfig.securityFilterChain(
                new HttpSecurity(
                        (ObjectPostProcessor<Object>) null, authenticationBuilder, new HashMap<>()));
    }

    /**
     * Test {@link SecurityConfig#authenticationManager(AuthenticationConfiguration)}.
     *
     * <p>Method under test: {@link SecurityConfig#authenticationManager(AuthenticationConfiguration)}
     */
    @Test
    @DisplayName("Test authenticationManager(AuthenticationConfiguration)")
    void testAuthenticationManager() throws Exception {
        // TODO: Diffblue Cover was only able to create a partial test for this method:
        //   Diffblue AI was unable to find a test
        // Arrange and Act
        securityConfig.authenticationManager(authenticationConfiguration);
    }
}
