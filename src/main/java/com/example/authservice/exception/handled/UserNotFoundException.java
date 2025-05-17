package com.example.authservice.exception.handled;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String mensaje) {
        super("Usuario no encontrado ´"+mensaje+"´");
    }
}