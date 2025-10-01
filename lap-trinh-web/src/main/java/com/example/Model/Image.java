package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    private int id;
    private String url;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

