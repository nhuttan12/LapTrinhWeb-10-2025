package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImage {
    private Integer id;
    private Integer imageId;
    private Integer productId;
    private ImageType type;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private Image image;
}

