package com.example.DTO.Products;

import com.example.Model.Image;
import com.example.Model.ProductImage;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class GetProductDetailResponseDTO {
    /**
     * Product
     */
    private Integer id;

    /**
     * ProductDetail
     */
    private String name;
    private Double price;
    private Double discount;

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

    private Map<String, Map<String, Object>> description;
    private String status;

    /**
     * Brand
     */
    private String brandName;

    /**
     * Image
     */
    private String thumbnailImages;
    private List<String> detailImages;
}
