package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetail {
    private int id;
    private int cartId;
    private int productId;
    private int quantity;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

