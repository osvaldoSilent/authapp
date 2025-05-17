// AuthController.java
package com.example.authservice.controller;

import com.example.authservice.dto.*;
import com.example.authservice.security.JwtUtil;
import com.example.authservice.service.UserService;
import com.example.authservice.model.User;
import com.example.authservice.security.JwtUtil;
import com.example.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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



    @GetMapping("/getUser")
    public UserFullResponseDTO getUser(@RequestBody UserDeleteRequestDTO userDTO) {
        return userService.getUserByUsername(userDTO.getUsername());

    }

    @DeleteMapping("/delete/user/{username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
        boolean deleted = userService.deleteByUsername(username);
        if (deleted) {
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }

    @DeleteMapping("/delete/user")
    public ResponseEntity<String> deleteUserByUsername(@RequestBody UserDeleteRequestDTO userD) {
        boolean deleted = userService.deleteByUsername(userD.getUsername());
        if (deleted) {
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }
    }


    @PutMapping("/update/user/")
    public ResponseEntity<String> updateUserByUsername(@RequestBody UserUpdateRequestDTO userDTO) {

        boolean updated = userService.updateByUsername(userDTO);

        if (updated) {
            return ResponseEntity.ok("Usuario actualizado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no actualizado");
        }
    }

}
