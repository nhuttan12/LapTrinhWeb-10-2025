package com.example.Service.User;

import com.example.DAO.UserDetailDAO;
import com.example.Model.UserDetail;

import java.sql.Connection;
import java.sql.SQLException;

public class UserDetailService {
    private final UserDetailDAO userDetailDAO;

    public UserDetailService() {
        this.userDetailDAO = new UserDetailDAO();
    }
    public UserDetailService(Connection conn) {
        this.userDetailDAO = new UserDetailDAO(conn);
    }

    public UserDetail getByUserId(int userId) {
        try {
            return userDetailDAO.getByUserId(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean insert(int userId, String phone, String address) {
        try {
            UserDetail detail = UserDetail.builder()
                    .userId(userId)
                    .phone(phone)
                    .address1(address)
                    .address2("")
                    .address3("")
                    .build();
            return userDetailDAO.insert(detail);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(int userId, String phone, String address) {
        try {
            UserDetail detail = UserDetail.builder()
                    .userId(userId)
                    .phone(phone)
                    .address1(address)
                    .address2("")
                    .address3("")
                    .build();
            return userDetailDAO.update(detail);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
