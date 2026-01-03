package com.example.DTO.Orders;

import com.example.Model.PaymentStatus;
import com.example.Model.ShippingStatus;
import lombok.Data;

@Data
public class GetOrdersPagingResponseAdminDTO {
    private int id;
    private String username;
    private String price;
    private PaymentStatus paymentStatus;
    private ShippingStatus shippingStatus;
    private String createdAt;
    private String updatedAt;
}
