package com.example.authservice.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CustomUserDetailsService.class})
@DisabledInAotMode
@ExtendWith(SpringExtension.class)
class CustomUserDetailsServiceDiffblueTest {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private UserRepository userRepository;

    /**
     * Test {@link CustomUserDetailsService#loadUserByUsername(String)}.
     *
     * <p>Method under test: {@link CustomUserDetailsService#loadUserByUsername(String)}
     */
    @Test
    @DisplayName("Test loadUserByUsername(String)")
    void testLoadUserByUsername() throws UsernameNotFoundException {
        // Arrange
        when(userRepository.findUserEntityByUsername(Mockito.<String>any()))
                .thenThrow(new UsernameNotFoundException("Usuario no encontrado"));

        // Act and Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("janedoe"));
        verify(userRepository).findUserEntityByUsername(eq("janedoe"));
    }

    /**
     * Test {@link CustomUserDetailsService#loadUserByUsername(String)}.
     *
     * <ul>
     *   <li>Given {@link User} {@link User#getPassword()} throw {@link
     *       UsernameNotFoundException#UsernameNotFoundException(String)} with {@code Msg}.
     * </ul>
     *
     * <p>Method under test: {@link CustomUserDetailsService#loadUserByUsername(String)}
     */
    @Test
    @DisplayName(
            "Test loadUserByUsername(String); given User getPassword() throw UsernameNotFoundException(String) with 'Msg'")
    void testLoadUserByUsername_givenUserGetPasswordThrowUsernameNotFoundExceptionWithMsg()
            throws UsernameNotFoundException {
        // Arrange
        User user = mock(User.class);
        when(user.getPassword()).thenThrow(new UsernameNotFoundException("Msg"));
        when(user.getUsername()).thenReturn("janedoe");
        doNothing().when(user).setId(Mockito.<String>any());
        doNothing().when(user).setPassword(Mockito.<String>any());
        doNothing().when(user).setRole(Mockito.<String>any());
        doNothing().when(user).setUsername(Mockito.<String>any());
        user.setId("42");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserEntityByUsername(Mockito.<String>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("janedoe"));
        verify(user).getPassword();
        verify(user).getUsername();
        verify(user).setId(eq("42"));
        verify(user).setPassword(eq("iloveyou"));
        verify(user).setRole(eq("Role"));
        verify(user).setUsername(eq("janedoe"));
        verify(userRepository).findUserEntityByUsername(eq("janedoe"));
    }

    /**
     * Test {@link CustomUserDetailsService#loadUserByUsername(String)}.
     *
     * <ul>
     *   <li>Given {@link User#User()} Id is {@code 42}.
     *   <li>Then return Authorities size is one.
     * </ul>
     *
     * <p>Method under test: {@link CustomUserDetailsService#loadUserByUsername(String)}
     */
    @Test
    @DisplayName(
            "Test loadUserByUsername(String); given User() Id is '42'; then return Authorities size is one")
    void testLoadUserByUsername_givenUserIdIs42_thenReturnAuthoritiesSizeIsOne()
            throws UsernameNotFoundException {
        // Arrange
        User user = new User();
        user.setId("42");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserEntityByUsername(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        UserDetails actualLoadUserByUsernameResult =
                customUserDetailsService.loadUserByUsername("janedoe");

        // Assert
        verify(userRepository).findUserEntityByUsername(eq("janedoe"));
        Collection<? extends GrantedAuthority> authorities =
                actualLoadUserByUsernameResult.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities instanceof Set);
        assertTrue(
                actualLoadUserByUsernameResult
                        instanceof org.springframework.security.core.userdetails.User);
        assertEquals("iloveyou", actualLoadUserByUsernameResult.getPassword());
        assertEquals("janedoe", actualLoadUserByUsernameResult.getUsername());
        assertTrue(actualLoadUserByUsernameResult.isAccountNonExpired());
        assertTrue(actualLoadUserByUsernameResult.isAccountNonLocked());
        assertTrue(actualLoadUserByUsernameResult.isCredentialsNonExpired());
        assertTrue(actualLoadUserByUsernameResult.isEnabled());
    }

    /**
     * Test {@link CustomUserDetailsService#loadUserByUsername(String)}.
     *
     * <ul>
     *   <li>Given {@link UserRepository} {@link UserRepository#findUserEntityByUsername(String)}
     *       return empty.
     * </ul>
     *
     * <p>Method under test: {@link CustomUserDetailsService#loadUserByUsername(String)}
     */
    @Test
    @DisplayName(
            "Test loadUserByUsername(String); given UserRepository findUserEntityByUsername(String) return empty")
    void testLoadUserByUsername_givenUserRepositoryFindUserEntityByUsernameReturnEmpty()
            throws UsernameNotFoundException {
        // Arrange
        Optional<User> emptyResult = Optional.empty();
        when(userRepository.findUserEntityByUsername(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("janedoe"));
        verify(userRepository).findUserEntityByUsername(eq("janedoe"));
    }

    /**
     * Test {@link CustomUserDetailsService#loadUserByUsername(String)}.
     *
     * <ul>
     *   <li>Then calls {@link User#getRole()}.
     * </ul>
     *
     * <p>Method under test: {@link CustomUserDetailsService#loadUserByUsername(String)}
     */
    @Test
    @DisplayName("Test loadUserByUsername(String); then calls getRole()")
    void testLoadUserByUsername_thenCallsGetRole() throws UsernameNotFoundException {
        // Arrange
        User user = mock(User.class);
        when(user.getRole()).thenThrow(new UsernameNotFoundException("Msg"));
        when(user.getPassword()).thenReturn("iloveyou");
        when(user.getUsername()).thenReturn("janedoe");
        doNothing().when(user).setId(Mockito.<String>any());
        doNothing().when(user).setPassword(Mockito.<String>any());
        doNothing().when(user).setRole(Mockito.<String>any());
        doNothing().when(user).setUsername(Mockito.<String>any());
        user.setId("42");
        user.setPassword("iloveyou");
        user.setRole("Role");
        user.setUsername("janedoe");
        Optional<User> ofResult = Optional.of(user);
        when(userRepository.findUserEntityByUsername(Mockito.<String>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("janedoe"));
        verify(user).getPassword();
        verify(user).getRole();
        verify(user).getUsername();
        verify(user).setId(eq("42"));
        verify(user).setPassword(eq("iloveyou"));
        verify(user).setRole(eq("Role"));
        verify(user).setUsername(eq("janedoe"));
        verify(userRepository).findUserEntityByUsername(eq("janedoe"));
    }
}
