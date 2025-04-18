package com.example.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponseLoginDto {

    private String username;
    private String role;
    private String token;



}
