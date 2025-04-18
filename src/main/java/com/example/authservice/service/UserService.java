
// UserService.java
package com.example.authservice.service;

import com.example.authservice.dto.UserRequestDTO;
import com.example.authservice.dto.UserRequestLoginDTO;
import com.example.authservice.dto.UserResponseDTO;
import com.example.authservice.dto.UserResponseLoginDto;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 📌 Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // 📌 Buscar usuario por username
    public User getUserByUsername(String username) {
        return (User) userRepository.findByUsername(username).orElse(null);
    }

    // 📌 Crear usuario (verificando que no exista)
    public User createUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        return userRepository.save(user);
    }

    // 📌 Eliminar usuario por ID
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    // 📌 Crear usuario (verificando que no exista)
    public UserResponseDTO register(UserRequestDTO dto) {


        Optional<User> existingUser = userRepository.findByUsername(dto.getUsername());
        if (existingUser.isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword())) // nunca guardes plaintext
                .role(dto.getRole() != null ? dto.getRole() : "USER")
                .build();

        userRepository.save(user);

        return new UserResponseDTO(user.getId(), user.getUsername(), user.getRole());
    }

    public UserResponseLoginDto login(UserRequestLoginDTO dto) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword())
            );

            if (!authentication.isAuthenticated()) {
                throw new BadCredentialsException("Invalid credentials");
            }

            User user = (User) userRepository.findByUsername(dto.getUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            String token = jwtUtil.generateToken(user.getUsername());

            return new UserResponseLoginDto(user.getUsername(), user.getRole(), token);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

}

