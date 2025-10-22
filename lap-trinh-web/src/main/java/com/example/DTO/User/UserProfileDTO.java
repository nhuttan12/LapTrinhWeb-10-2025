package com.example.DTO.User;

import lombok.Data;

@Data
public class UserProfileDTO {
    Integer id;
    String fullName;
    String phone;
    String email;
    String address1;
    String address2;
    String address3;
    String avatar;
}
