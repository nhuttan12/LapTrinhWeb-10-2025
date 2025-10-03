package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDetail {
    private Integer id;
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
    private CartDetailStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
