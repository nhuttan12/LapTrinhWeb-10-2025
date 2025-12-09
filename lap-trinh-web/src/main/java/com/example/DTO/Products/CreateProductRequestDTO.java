package com.example.DTO.Products;

import com.example.Model.ProductStatus;
import lombok.Data;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequestDTO {

    // product
    private String name;
    private Double price;
    private Double discount;
    private String category;
    private ProductStatus status;

    // product detail
    private String os;
    private Integer ram;
    private Integer storage;
    private Integer batteryCapacity;
    private Double screenSize;
    private String screenResolution;
    private String mobileNetwork;
    private String cpu;
    private String gpu;
    private String waterResistance;
    private Integer maxChargeWatt;
    private String design;
    private String memoryCard;
    private Double cpuSpeed;
    private Timestamp releaseDate;
    private Double rating;
    private String description;

    // brand
    private Integer brandId;

    // thumbnail
    private String thumbnail;
    private List<String> detailImage;
}
