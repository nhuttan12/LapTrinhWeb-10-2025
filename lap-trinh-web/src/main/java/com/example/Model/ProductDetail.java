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
    private String os;
    private String ram;
    private String storage;
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
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private Brand brand;
}

