package com.example.authservice.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.time.LocalDateTime;

@Data
public class SaveProductRequestDto {

    @NotBlank(message = "Product name is required")
    @NotNull(message = "Product name is required")
    private String name;

    private String description;

    @NotBlank(message = "Category is required")
    @NotNull(message = "Category is required")
    private String category;

    private boolean isActive;

    private LocalDateTime createdAt;


}
