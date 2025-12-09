package com.example.Model;

import java.sql.Timestamp;
import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
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
    private String thumbnail;
}
