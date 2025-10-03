package com.example.Model;

import java.sql.Timestamp;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Integer id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private UserStatus status;
    private Integer roleId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}