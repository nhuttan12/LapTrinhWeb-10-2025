package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    private int id;
    private String name;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

