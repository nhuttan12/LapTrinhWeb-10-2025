package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {
    private Integer id;
    private String url;
    private ImageStatus status;
    private ImageType type;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

