package com.example.DTO.Products;

import com.example.Model.ProductStatus;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
public class UpdateProductRequestDTO {
    private Integer id;

    private String name;
    private Double price;
    private Double discount;
    private ProductStatus status;

    // detail
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

    private Integer brandId;

    private String thumbnail;
}
