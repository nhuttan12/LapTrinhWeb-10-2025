package com.example.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner {
    private Integer id;
    private Image image;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
