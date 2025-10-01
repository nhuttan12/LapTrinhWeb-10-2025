package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserImage {
    private int id;
    private int userId;
    private int imageId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

