package com.example.DTO.Order;

import lombok.Data;

@Data
public class OrderUserResponseDTO {
    private String id;
    private int totalPrice;
    private String status;
    private String createdAt;
}
