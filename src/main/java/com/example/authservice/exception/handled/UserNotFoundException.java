package com.example.authservice.exception.handled;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("Usuario no encontrado ´"+username+"´");
    }
}