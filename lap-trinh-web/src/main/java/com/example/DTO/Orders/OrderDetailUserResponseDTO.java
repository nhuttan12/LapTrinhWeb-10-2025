package com.example.DTO.Orders;

import lombok.Data;

@Data
public class OrderDetailUserResponseDTO {
    private int productName;
    private int quantity;
    private String productImage;
    private double price;
    private double totalPrice;
}
