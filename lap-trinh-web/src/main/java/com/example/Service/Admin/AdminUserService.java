package com.example.Service.Admin;

import com.example.DAO.AdminUserDAO;
import com.example.Model.RoleName;
import com.example.Model.User;

import java.sql.SQLException;
import java.util.List;

public class AdminUserService {
    private final AdminUserDAO adminUserDAO;

    public AdminUserService(AdminUserDAO adminUserDAO) {
        this.adminUserDAO = adminUserDAO;
    }

    /**
     * Lấy tất cả user
     */
    public List<User> getAllUsers() {
        try {
            return adminUserDAO.getAllUsers();
        } catch (SQLException e) {
            e.printStackTrace();
            return List.of(); // trả về danh sách rỗng nếu lỗi
        }
    }

    /**
     * Xóa mềm / phục hồi user theo userId
     */
    public boolean softDeleteUser(int userId) {
        try {
            return adminUserDAO.toggleUserStatus(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Thay đổi role user
     */
    public boolean changeUserRole(int userId, RoleName newRoleName) {
        try {
            return adminUserDAO.changeUserRole(userId, newRoleName);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
