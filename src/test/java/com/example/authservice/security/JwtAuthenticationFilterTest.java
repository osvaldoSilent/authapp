package com.example.authservice.security;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


class JwtAuthenticationFilterTest {

    private JwtService jwtService;
    private CustomUserDetailsService customUserDetailsService;
    //private JwtAuthenticationFilter filter;

    private final String token = "fake-jwt-token";
    private final String username = "osvaldo";
    private final String role = "ROLE_ADMIN";

    @BeforeEach
    void setUp() {
        jwtService = mock(JwtService.class);
        customUserDetailsService = mock(CustomUserDetailsService.class);
        //filter = new JwtAuthenticationFilter(jwtService, customUserDetailsService);
        SecurityContextHolder.clearContext(); // Limpiar antes de cada test
    }

    @Test
    void shouldAuthenticateWhenValidTokenPresent() throws ServletException, IOException {
        // Mocks
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(username);

        List lista= List.of(new SimpleGrantedAuthority("ROLE_USER"));
        when(userDetails.getAuthorities()).thenReturn(lista);

        when(jwtService.extractUsername(token)).thenReturn(username);
        when(jwtService.extractRol(token)).thenReturn(role);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        // Act
        //filter.doFilterInternal(request, response, chain);

        // Assert
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(authentication);
        assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
        assertEquals(userDetails, authentication.getPrincipal());
        assertTrue(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    void shouldSkipWhenNoAuthorizationHeader() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest(); // No header
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = mock(MockFilterChain.class);

        //filter.doFilterInternal(request, response, chain);

        // No autenticación esperada
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain, times(1)).doFilter(request, response);
    }

    @Test
    void shouldSkipWhenTokenInvalid() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = mock(MockFilterChain.class);

        UserDetails userDetails = mock(UserDetails.class);
        when(jwtService.extractUsername(token)).thenReturn(username);
        when(jwtService.extractRol(token)).thenReturn(role);
        when(customUserDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false); // <== invalida

        //filter.doFilterInternal(request, response, chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(chain, times(1)).doFilter(request, response);
    }
}
