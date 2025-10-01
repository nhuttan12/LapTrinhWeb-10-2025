package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail {
    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    private double price;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

