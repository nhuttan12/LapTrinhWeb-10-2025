package com.example.Model;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    private Integer id;
    private Integer userId;
    private Double price;
    private ShippingStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
