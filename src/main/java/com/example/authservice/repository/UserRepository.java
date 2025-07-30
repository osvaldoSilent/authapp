
// UserRepository.java
package com.example.authservice.repository;

import com.example.authservice.dto.user.UserFullResponseDTO;
import com.example.authservice.model.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<UserFullResponseDTO> findDtoByUsername(String username);
    Optional <User> findUserEntityByUsername(String username);
}

