package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItem {
    private int id;
    private int productId;
    private int userId;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

