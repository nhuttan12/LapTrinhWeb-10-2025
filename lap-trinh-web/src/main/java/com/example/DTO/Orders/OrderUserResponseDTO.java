package com.example.DTO.Orders;

import com.example.Model.PaymentStatus;
import com.example.Model.ShippingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class OrderUserResponseDTO {
    private String id;
    private int totalPrice;
    private PaymentStatus paymentStatus;
    private ShippingStatus shippingStatus;
//    private String status;
    private Timestamp createdAt;
}
