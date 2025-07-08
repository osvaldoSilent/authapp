package com.example.authservice.controller;

import com.example.authservice.repository.LambdaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LambdaController {

    private final LambdaRepository lambdaClient;

    public LambdaController(LambdaRepository lambdaClient) {
        this.lambdaClient = lambdaClient;
    }

    @GetMapping("/hola-lambda")
    public ResponseEntity<String> consumirLambda() {
        String respuesta = lambdaClient.getHolaDesdeLambda();
        return ResponseEntity.ok(respuesta);
    }
}

