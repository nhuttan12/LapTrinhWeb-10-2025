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
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();

        String sql = """
            SELECT 
                u.id AS user_id,
                u.username,
                u.full_name,
                u.email,
                u.status AS user_status,
                u.role_id,
                u.created_at AS user_created,
                u.updated_at AS user_updated,

                d.phone,
                d.address_1,

                ui.id AS user_image_id,
                ui.image_id AS linked_image_id,
                ui.created_at AS user_image_created,
                ui.updated_at AS user_image_updated,

                i.id AS image_id,
                i.url AS image_url,
                i.status AS image_status,
                i.created_at AS image_created,
                i.updated_at AS image_updated

            FROM users u
            LEFT JOIN user_details d ON u.id = d.user_id
            LEFT JOIN user_images ui ON u.id = ui.user_id
            LEFT JOIN images i ON ui.image_id = i.id
            WHERE u.deleted_at IS NULL
            ORDER BY u.id ASC
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // --- User ---
                User user = new User();
                user.setId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("full_name"));
                user.setEmail(rs.getString("email"));
                user.setRoleId(rs.getInt("role_id"));
                user.setCreatedAt(rs.getTimestamp("user_created"));
                user.setUpdatedAt(rs.getTimestamp("user_updated"));

                String status = rs.getString("user_status");
                if (status != null) {
                    user.setStatus(UserStatus.valueOf(status.toUpperCase()));
                }

                // --- UserDetail ---
                UserDetail detail = new UserDetail();
                detail.setPhone(rs.getString("phone"));
                detail.setAddress1(rs.getString("address_1"));
                user.setUserDetail(detail);

                // --- Image ---
                Image image = new Image();
                image.setId(rs.getInt("image_id"));
                image.setUrl(rs.getString("image_url"));
                String imgStatus = rs.getString("image_status");
                if (imgStatus != null) {
                    image.setStatus(ImageStatus.valueOf(imgStatus.toUpperCase()));
                }
                image.setCreatedAt(rs.getTimestamp("image_created"));
                image.setUpdatedAt(rs.getTimestamp("image_updated"));

                // --- UserImage ---
                UserImage userImage = new UserImage();
                userImage.setId(rs.getInt("user_image_id"));
                userImage.setUserId(user.getId());
                userImage.setImageId(rs.getInt("linked_image_id"));
                userImage.setCreatedAt(rs.getTimestamp("user_image_created"));
                userImage.setUpdatedAt(rs.getTimestamp("user_image_updated"));
                userImage.setImage(image);

                user.setUserImage(userImage);

                list.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error getting all users", e);
        }

        return list;
    }

    // 2️⃣ Soft delete user
    public boolean softDeleteUser(int userId) {
        String sql = "UPDATE users SET deleted_at = NOW(), status = 'INACTIVE' WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error soft deleting user", e);
        }
    }

    // 3️⃣ Đổi role user
    public boolean changeUserRole(int userId, int newRoleId) {
        String sql = "UPDATE users SET role_id = ?, updated_at = NOW() WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newRoleId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error changing user role", e);
        }
    }
}
