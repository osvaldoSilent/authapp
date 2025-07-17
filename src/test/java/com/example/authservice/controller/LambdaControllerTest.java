package com.example.authservice.controller;

import com.example.authservice.repository.LambdaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LambdaControllerTest {

    private LambdaRepository lambdaClient;
    private LambdaController controller;

    @BeforeEach
    void setUp() {
        lambdaClient = mock(LambdaRepository.class);
        controller = new LambdaController(lambdaClient);
    }

    @Test
    void consumirLambda_shouldReturnOkWithBody() {
        // Arrange
        String expected = "¡Hola desde Lambda!";
        when(lambdaClient.getHolaDesdeLambda()).thenReturn(expected);

        // Act
        ResponseEntity<String> response = controller.consumirLambda();

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(expected, response.getBody());
        verify(lambdaClient, times(1)).getHolaDesdeLambda();
    }

    @Test
    void consumirLambda_whenLambdaThrowsException_shouldPropagate() {
        // Arrange
        when(lambdaClient.getHolaDesdeLambda())
                .thenThrow(new RuntimeException("Lambda falló"));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            controller.consumirLambda();
        });
        assertEquals("Lambda falló", ex.getMessage());
    }
}
