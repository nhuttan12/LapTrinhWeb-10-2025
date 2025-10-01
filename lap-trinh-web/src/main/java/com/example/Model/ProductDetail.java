package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetail {
    private int id;
    private String size;
    private String color;
    private String description;
    private double rating;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

