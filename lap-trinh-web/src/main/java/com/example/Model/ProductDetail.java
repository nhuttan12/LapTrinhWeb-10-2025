package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDetail {
    private Integer id;
    private Integer productId;
    private String size;
    private String color;
    private String description;
    private Double rating;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
