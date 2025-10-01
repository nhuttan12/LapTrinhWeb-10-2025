package com.example.Model;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private int id;
    private int userId;
    private double price;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

