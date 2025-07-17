package com.example.authservice.exception.handled;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("Credenciales invalidas");
    }
}
