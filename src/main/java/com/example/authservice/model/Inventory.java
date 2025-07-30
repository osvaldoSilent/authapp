// User.java
package com.example.authservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("inventories")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inventory {

    @Id
    private String id;

    @Indexed
    private String productId; // Referencia al producto

    private int stock;

    private double price;

    private boolean isAvailable;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}



/*
* {
  "_id": "ObjectId",
  "userId": "ObjectId",         // Referencia al usuario que ordenó
  "items": [
    {
      "productId": "ObjectId",  // Referencia a inventory
      "quantity": 2,
      "unitPrice": 150.0,
      "total": 300.0
    }
  ],
  "status": "PENDING",          // PENDING, PAID, SHIPPED, CANCELLED
  "createdAt": "2025-07-29T16:00:00Z",
  "updatedAt": "2025-07-29T16:15:00Z"
}
* */