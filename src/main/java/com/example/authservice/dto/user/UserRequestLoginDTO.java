package com.example.authservice.dto.user;

import lombok.Data;

@Data
public class UserRequestLoginDTO {
    private String username;
    private String password;
}
