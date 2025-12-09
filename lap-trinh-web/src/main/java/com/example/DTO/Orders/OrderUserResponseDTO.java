package com.example.DTO.Orders;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderUserResponseDTO {
    private String id;
    private int totalPrice;
    private String status;
    private Timestamp createdAt;
}
