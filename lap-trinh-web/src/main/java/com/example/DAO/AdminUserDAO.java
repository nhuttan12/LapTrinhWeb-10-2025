package com.example.DAO;

import com.example.Model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminUserDAO {
    private final Connection conn;
    private static final int DEFAULT_IMAGE_ID = 1;

    public AdminUserDAO(Connection conn) {
        this.conn = conn;
    }

    //lay thong tin user
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT " +
                "u.id AS user_id, u.username, u.full_name, u.email, u.status AS user_status, " +
                "u.role_id, u.created_at AS user_created_at, u.updated_at AS user_updated_at, " +
                "r.id AS role_id, r.name AS role_name, r.status AS role_status, " +
                "ud.id AS detail_id, ud.phone, ud.address_1, ud.address_2, ud.address_3, " +
                "ud.created_at AS detail_created_at, ud.updated_at AS detail_updated_at " +
                "FROM users u " +
                "LEFT JOIN roles r ON u.role_id = r.id " +
                "LEFT JOIN user_details ud ON u.id = ud.user_id " +
                "WHERE u.status IS NULL OR LOWER(u.status) != 'inactive'";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UserStatus userStatus = UserStatus.ACTIVE;
                String statusStr = rs.getString("user_status");
                if (statusStr != null) {
                    try {
                        userStatus = UserStatus.fromString(statusStr);
                    } catch (IllegalArgumentException e) {
                        userStatus = UserStatus.ACTIVE;
                    }
                }

                User user = User.builder()
                        .id(rs.getInt("user_id"))
                        .username(rs.getString("username"))
                        .fullName(rs.getString("full_name"))
                        .email(rs.getString("email"))
                        .status(userStatus)
                        .roleId(rs.getInt("role_id"))
                        .createdAt(rs.getTimestamp("user_created_at"))
                        .updatedAt(rs.getTimestamp("user_updated_at"))
                        .build();

                // Role mapping
                Role role = null;
                String roleName = rs.getString("role_name");
                if (roleName != null) {
                    role = Role.builder()
                            .id(rs.getInt("role_id"))
                            .name(RoleName.fromString(roleName))
                            .status(RoleStatus.fromString(rs.getString("role_status")))
                            .build();
                }

                // UserDetail mapping
                UserDetail detail = null;
                if (rs.getObject("detail_id") != null) {
                    detail = UserDetail.builder()
                            .id(rs.getInt("detail_id"))
                            .userId(rs.getInt("user_id"))
                            .phone(rs.getString("phone"))
                            .address1(rs.getString("address_1"))
                            .address2(rs.getString("address_2"))
                            .address3(rs.getString("address_3"))
                            .createdAt(rs.getTimestamp("detail_created_at"))
                            .updatedAt(rs.getTimestamp("detail_updated_at"))
                            .build();
                }

                user.setUserDetail(detail);
                user.setUserImage(null);
                users.add(user);
            }
        }

        return users;
    }


    // Soft delete user
    public boolean toggleUserStatus(int userId) throws SQLException {
        String currentStatusSql = "SELECT status FROM users WHERE id = ?";
        String newStatus = null;
        try (PreparedStatement stmt = conn.prepareStatement(currentStatusSql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString("status");
                    if ("active".equalsIgnoreCase(status)) {
                        newStatus = UserStatus.INACTIVE.getUserStatus(); // "inactive"
                    } else {
                        newStatus = UserStatus.ACTIVE.getUserStatus(); // "active"
                    }
                } else return false;
            }
        }

        String updateSql = "UPDATE users SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setString(1, newStatus); // truyền chữ thường
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }



    // Change role: đổi role dựa trên RoleName enum
    public boolean changeUserRole(int userId, RoleName newRoleName) throws SQLException {
        // 1. Lấy role_id từ DB theo name (chữ thường)
        String sqlRole = "SELECT id FROM roles WHERE name = ?";
        int roleId = -1;
        try (PreparedStatement stmt = conn.prepareStatement(sqlRole)) {
            stmt.setString(1, newRoleName.getRoleName()); // dùng chữ thường
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) roleId = rs.getInt("id");
                else return false; // role không tồn tại
            }
        }

        // 2. Update role_id cho user
        String sqlUpdate = "UPDATE users SET role_id = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
            stmt.setInt(1, roleId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }

}
