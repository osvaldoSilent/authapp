
// UserService.java
package com.example.authservice.service;

import com.example.authservice.dto.product.BasicProductResponseDto;

import com.example.authservice.dto.product.SaveProductRequestDto;
import com.example.authservice.exception.handled.UserAlreadyExistsException;
import com.example.authservice.model.Product;
import com.example.authservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<BasicProductResponseDto> getAllProducts() {

        return productRepository.findAll().stream()
                .map(product -> new BasicProductResponseDto(
                        product.getName(),
                        product.getDescription()
                ))
                .collect(Collectors.toList());
    }

    public boolean addProduct(SaveProductRequestDto dto) {

        Optional<BasicProductResponseDto> existingUser = productRepository.findBasicProductDTOByName(dto.getName());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(dto.getName());
        }
        Product product = Product
                .builder()
                .name(dto.getName().trim())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .isActive(dto.isActive())
                .createdAt(LocalDateTime.now())
                .build();

        productRepository.save(product);

        return true;
    }

}

