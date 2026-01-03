package com.example.DTO.Orders;

import com.example.Model.PaymentStatus;
import com.example.Model.ShippingStatus;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class OrderUserResponseDTO {
    private String id;
    private int totalPrice;
    private PaymentStatus paymentStatus;
    private ShippingStatus shippingStatus;
//    private String status;
    private Timestamp createdAt;
}
