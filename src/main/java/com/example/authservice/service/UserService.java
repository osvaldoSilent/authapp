
// UserService.java
package com.example.authservice.service;

import com.example.authservice.dto.*;
import com.example.authservice.exception.handled.UserNotFoundException;
import com.example.authservice.model.User;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

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
    public UserFullResponseDTO getUserByUsername(String username) {
        return userRepository.findDtoByUsername(username)
                        .orElseThrow(() -> (RuntimeException) new UserNotFoundException(username));
    }

    // 📌 Crear usuario (verificando que no exista)
    public User createUser(User user) {
        Optional<UserFullResponseDTO> existingUser = userRepository.findDtoByUsername(user.getUsername());
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


        Optional<UserFullResponseDTO> existingUser = userRepository.findDtoByUsername(dto.getUsername());
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

            UserFullResponseDTO user = (UserFullResponseDTO) userRepository.findDtoByUsername(dto.getUsername())
                    .orElseThrow(() -> (RuntimeException) new UserNotFoundException(dto.getUsername()));

            String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

            return new UserResponseLoginDto(user.getUsername(), user.getRole(), token);

        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado: " + e.getMessage());
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public boolean deleteByUsername(String username) {
        Optional<User> userOptional = userRepository.findUserEntityByUsername(username);

        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public boolean deleteByUsername(UserDeleteRequestDTO user) {
        Optional<User> userOptional =  userRepository.findUserEntityByUsername(user.getUsername());

        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public boolean updateByUsername(String username) {
        Optional<User> userOptional = userRepository.findUserEntityByUsername(username);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return true;
        } else {
            return false;
        }
    }

    public boolean updateByUsername(UserUpdateRequestDTO userDTO) {
        Optional<User> userOptional = userRepository.findUserEntityByUsername(userDTO.getCurrentUserName());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if(StringUtils.hasText(userDTO.getNewUserName())){
                if (!userDTO.getNewUserName().equals(user.getUsername()) &&
                        userRepository.findDtoByUsername(userDTO.getNewUserName()).isPresent()) {
                    throw new RuntimeException("El nuevo username ya está en uso");
                }
                user.setUsername(userDTO.getNewUserName());
            }
            if(StringUtils.hasText(userDTO.getNewPassword())){

                user.setPassword(passwordEncoder.encode(userDTO.getNewPassword()));
            }
            if(StringUtils.hasText(userDTO.getNewRole())){
                user.setRole(userDTO.getNewRole());
            }
            userRepository.save(user);
            return true;
        } else {
            return false;
        }
    }
}

