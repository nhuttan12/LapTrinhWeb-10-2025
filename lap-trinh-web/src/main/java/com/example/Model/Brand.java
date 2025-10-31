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
public class Brand {
    private Integer id;
    private String name;
    private BrandStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
