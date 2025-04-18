package com.example.authservice.dto;

import lombok.Data;

@Data
public class UserRequestLoginDTO {
    private String username;
    private String password;
}
