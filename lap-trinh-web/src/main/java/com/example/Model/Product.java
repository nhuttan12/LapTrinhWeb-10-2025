package com.example.Model;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    private Integer id;
    private String name;
    private Double price;
    private Double discount;
    private ProductStatus status;
    private String category;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private List<ProductImage> productImages;
    private ProductDetail productDetail;
}
