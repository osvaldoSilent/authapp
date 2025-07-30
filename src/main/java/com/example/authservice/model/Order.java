// User.java
package com.example.authservice.model;

import com.example.authservice.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("orders")
public class Order {

    @Id
    private String id;

    @Indexed
    private String userId; // Referencia al usuario

    private List<Item> items;

    private OrderStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- Constructors, Getters, Setters, etc. ---

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private String productId;
        private int quantity;
        private double unitPrice;
        private double total;
    }
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