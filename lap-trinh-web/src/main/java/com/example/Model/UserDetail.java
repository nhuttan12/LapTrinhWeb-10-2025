package com.example.Model;

import java.sql.Timestamp;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetail {
    private Integer id;
    private Integer userId;
    private String phone;
    private String address1;
    private String address2;
    private String address3;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    public String getAddress() {
        return (address1 == null ? "" : address1) +
                (address2 == null ? "" : ", " + address2) +
                (address3 == null ? "" : ", " + address3);
    }

}
