// User.java
package com.example.authservice.model;

import com.example.authservice.enums.PaymentMethod;
import com.example.authservice.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("payments")
public class Payment {

    @Id
    private String id;

    @Indexed
    private String orderId;

    private String userId;

    private double amount;

    private PaymentMethod method;
    private PaymentStatus status;


    private String transactionId;

    private LocalDateTime paidAt;

    // --- Constructors, Getters, Setters ---
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