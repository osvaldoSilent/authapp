package com.example.authservice.dto;

import lombok.Data;

@Data
public class UserUpdateRequestDTO {
    String currentUserName;
    String currentPassword;
    String currentRole;
    String newUserName;
    String newPassword;
    String newRole;
}
