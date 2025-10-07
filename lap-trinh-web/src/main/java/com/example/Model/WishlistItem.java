package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItem {
    private Integer id;
    private Integer productId;
    private Integer userId;
    private WishlistStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}