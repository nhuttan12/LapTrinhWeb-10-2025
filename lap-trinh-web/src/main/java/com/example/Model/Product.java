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
public class Product {
    private int id;
    private String name;
    private double price;
    private double discount;
    private String status;
    private String category;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

