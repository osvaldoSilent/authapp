// AuthController.java
package com.example.authservice.controller;

import com.example.authservice.dto.UserRequestDTO;
import com.example.authservice.dto.UserRequestLoginDTO;
import com.example.authservice.dto.UserResponseDTO;
import com.example.authservice.dto.UserResponseLoginDto;
import com.example.authservice.security.JwtUtil;
import com.example.authservice.service.UserService;
import com.example.authservice.model.User;
import com.example.authservice.security.JwtUtil;
import com.example.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/register2")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO dto) {
        UserResponseDTO response = userService.register(dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseLoginDto> login(@RequestBody UserRequestLoginDTO user) {
        UserResponseLoginDto response = userService.login(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/dummytest")
    public String dummy(@RequestBody UserRequestLoginDTO user) {
        return "test";
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
