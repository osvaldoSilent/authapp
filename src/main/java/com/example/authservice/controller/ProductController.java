package com.example.authservice.controller;

import com.example.authservice.dto.product.BasicProductResponseDto;
import com.example.authservice.dto.product.SaveProductRequestDto;
import com.example.authservice.dto.user.*;
import com.example.authservice.model.User;
import com.example.authservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping("/add")
    public ResponseEntity<Boolean> addProduct(@Valid @RequestBody SaveProductRequestDto dto) {
        boolean response = productService.addProduct(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<BasicProductResponseDto>> getAllProducts() {
        List<BasicProductResponseDto> response = productService.getAllProducts();
        return ResponseEntity.of(Optional.ofNullable(response));
    }

}
