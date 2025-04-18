
// UserRepository.java
package com.example.authservice.repository;

import com.example.authservice.model.User;
//import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional findByUsername(String username);
}

