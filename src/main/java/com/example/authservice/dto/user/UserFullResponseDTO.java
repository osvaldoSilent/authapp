package com.example.authservice.dto.user;

import lombok.Data;

@Data
public class UserFullResponseDTO {
    private String id;
    private String username;
    private String role;
    private String password;
}
