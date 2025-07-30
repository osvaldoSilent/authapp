
// UserRepository.java
package com.example.authservice.repository;

import com.example.authservice.dto.product.BasicProductResponseDto;
import com.example.authservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<BasicProductResponseDto> findBasicProductDTOByName(String name);
    Optional <Product> findProductEntityByName(String name);
    List<Product> findAll();
}

