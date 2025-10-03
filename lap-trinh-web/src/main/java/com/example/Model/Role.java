package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    private Integer id;
    private RoleName name;
    private RoleStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}

