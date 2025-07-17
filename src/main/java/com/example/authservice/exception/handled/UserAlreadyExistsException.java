package com.example.authservice.exception.handled;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("Usuario ´"+username+"´ ya existe");
    }
}
