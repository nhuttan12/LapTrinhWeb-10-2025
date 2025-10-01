package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    private int id;
    private int imageId;
    private int productId;
    private String type;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

