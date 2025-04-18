// User.java
package com.example.authservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

//@Entity
//@Table(name = "app_user") // Evitar el uso de "user"
//@Document(collection = "app_user") // Nombre de la colección en MongoDB
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "app_user") // Define la colección en MongoDB
public class User {
    //@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private String id; // MongoDB usa ObjectId en String
    @Indexed(unique = true) // Evita duplicados en username
    private String username;
    private String password;
    private String role;
}
