package com.example.DAO;

import com.example.Model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminUserDAO {
    private final Connection conn;
    private static final int DEFAULT_IMAGE_ID = 1;

    public AdminUserDAO(Connection conn) {
        this.conn = conn;
    }

    // =====================================================
    // LẤY DANH SÁCH USER (CÓ ẢNH, ROLE, DETAIL)
    // =====================================================
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();

        String sql = "SELECT " +
                "u.id AS user_id, u.username, u.full_name, u.email, u.status AS user_status, " +
                "u.role_id, u.created_at AS user_created_at, u.updated_at AS user_updated_at, " +
                "r.id AS role_id, r.name AS role_name, r.status AS role_status, " +
                "ud.id AS detail_id, ud.phone, ud.address_1, ud.address_2, ud.address_3, " +
                "ud.created_at AS detail_created_at, ud.updated_at AS detail_updated_at, " +
                "ui.id AS user_image_id, ui.image_id AS user_image_image_id, " +
                "ui.created_at AS user_image_created_at, ui.updated_at AS user_image_updated_at, " +
                "i.id AS image_id, i.url AS image_url, i.status AS image_status, " +
                "i.created_at AS image_created_at, i.updated_at AS image_updated_at " +
                "FROM users u " +
                "LEFT JOIN roles r ON u.role_id = r.id " +
                "LEFT JOIN user_details ud ON u.id = ud.user_id " +
                "LEFT JOIN user_images ui ON u.id = ui.user_id " +
                "  AND ui.id = (SELECT MAX(ui2.id) FROM user_images ui2 WHERE ui2.user_id = u.id) " +
                "LEFT JOIN images i ON ui.image_id = i.id " ;
//                "WHERE u.status IS NULL OR LOWER(u.status) != 'inactive'";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // --- Xử lý trạng thái ---
                UserStatus userStatus = UserStatus.ACTIVE;
                String statusStr = rs.getString("user_status");
                if (statusStr != null) {
                    try {
                        userStatus = UserStatus.fromString(statusStr);
                    } catch (IllegalArgumentException ignored) {
                        userStatus = UserStatus.ACTIVE;
                    }
                }

                // --- Tạo user ---
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

                // --- Role ---
                String roleName = rs.getString("role_name");
                if (roleName != null) {
                    Role role = Role.builder()
                            .id(rs.getInt("role_id"))
                            .name(RoleName.fromString(roleName))
                            .status(RoleStatus.fromString(rs.getString("role_status")))
                            .build();
                    // Nếu bạn muốn set role vào user, thêm field Role trong model User
                }

                // --- UserDetail ---
                if (rs.getObject("detail_id") != null) {
                    UserDetail detail = UserDetail.builder()
                            .id(rs.getInt("detail_id"))
                            .userId(rs.getInt("user_id"))
                            .phone(rs.getString("phone"))
                            .address1(rs.getString("address_1"))
                            .address2(rs.getString("address_2"))
                            .address3(rs.getString("address_3"))
                            .createdAt(rs.getTimestamp("detail_created_at"))
                            .updatedAt(rs.getTimestamp("detail_updated_at"))
                            .build();
                    user.setUserDetail(detail);
                }

                // --- Image ---
                Image image;
                if (rs.getObject("image_id") != null) {
                    try {
                        image = Image.builder()
                                .id(rs.getInt("image_id"))
                                .url(rs.getString("image_url"))
                                .status(ImageStatus.fromString(rs.getString("image_status")))
                                .createdAt(rs.getTimestamp("image_created_at"))
                                .updatedAt(rs.getTimestamp("image_updated_at"))
                                .build();
                    } catch (Exception e) {
                        image = getDefaultImage();
                    }
                } else {
                    image = getDefaultImage();
                }

                // --- UserImage ---
                UserImage userImage = UserImage.builder()
                        .id(rs.getObject("user_image_id") != null ? rs.getInt("user_image_id") : null)
                        .userId(rs.getInt("user_id"))
                        .imageId(image.getId())
                        .createdAt(rs.getTimestamp("user_image_created_at"))
                        .updatedAt(rs.getTimestamp("user_image_updated_at"))
                        .image(image)
                        .build();

                user.setUserImage(userImage);

                users.add(user);
            }
        }

        return users;
    }

    // Ảnh mặc định
    private Image getDefaultImage() {
        return Image.builder()
                .id(DEFAULT_IMAGE_ID)
                .url("/images/default_avatar.png")
                .status(ImageStatus.ACTIVE)
                .build();
    }

    // =====================================================
    // ĐỔI TRẠNG THÁI USER (SOFT DELETE / RESTORE)
    // =====================================================
    public boolean toggleUserStatus(int userId) throws SQLException {
        String currentStatusSql = "SELECT status FROM users WHERE id = ?";
        String newStatus;

        try (PreparedStatement stmt = conn.prepareStatement(currentStatusSql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (!rs.next()) return false;
                String status = rs.getString("status");
                if ("active".equalsIgnoreCase(status)) {
                    newStatus = UserStatus.INACTIVE.getUserStatus();
                } else {
                    newStatus = UserStatus.ACTIVE.getUserStatus();
                }
            }
        }

        String updateSql = "UPDATE users SET status = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    // =====================================================
    // ĐỔI ROLE USER
    // =====================================================
    public boolean changeUserRole(int userId, RoleName newRoleName) throws SQLException {
        // 1. Lấy role_id
        String sqlRole = "SELECT id FROM roles WHERE name = ?";
        int roleId = -1;
        try (PreparedStatement stmt = conn.prepareStatement(sqlRole)) {
            stmt.setString(1, newRoleName.getRoleName());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    roleId = rs.getInt("id");
                } else {
                    return false;
                }
            }
        }

        // 2. Cập nhật role_id
        String sqlUpdate = "UPDATE users SET role_id = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sqlUpdate)) {
            stmt.setInt(1, roleId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }
}
