package com.example.DTO.Products;

import lombok.Data;

@Data
public class GetProductsPagingResponseDTO {
    private int id;
    private String name;
    private double price;
    private double discount;
    private String description;
    private String thumbnail;
}
