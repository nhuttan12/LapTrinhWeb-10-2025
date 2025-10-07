package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {
    private Integer id;
    private Integer userId;
    private CartStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
