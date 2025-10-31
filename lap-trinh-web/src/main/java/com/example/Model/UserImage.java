package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserImage {
    private Integer id;
    private Integer userId;
    private Integer imageId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    private Image image;
}