package com.example.DAO;

import com.example.Model.User;
import com.example.Model.UserDetail;
import com.example.Model.UserStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection conn;
    private static final int DEFAULT_IMAGE_ID = 1;

    public UserDAO(Connection conn) {
        this.conn = conn;
    }

    public User getUserProfile(int userId) throws SQLException {
        String sql = "SELECT u.id, u.username, u.full_name, u.email, u.role_id, u.status, " +
                "u.created_at, u.updated_at, " +
                "ud.id AS ud_id, ud.address_1, ud.address_2, " +
                "ud.created_at AS ud_created, ud.updated_at AS ud_updated " +
                "FROM users u " +
                "LEFT JOIN user_details ud ON u.id = ud.user_id " +
                "WHERE u.id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserDetail details = UserDetail.builder()
                        .id(rs.getInt("ud_id"))
                        .address1(rs.getString("address_1"))
                        .address2(rs.getString("address_2"))
                        .createdAt(rs.getTimestamp("ud_created"))
                        .updatedAt(rs.getTimestamp("ud_updated"))
                        .build();

                return User.builder()
                        .id(rs.getInt("id"))
                        .username(rs.getString("username"))
                        .fullName(rs.getString("full_name"))
                        .email(rs.getString("email"))
                        .status(UserStatus.valueOf(rs.getString("status").toUpperCase()))
                        .roleId(rs.getInt("role_id"))
                        .createdAt(rs.getTimestamp("created_at"))
                        .updatedAt(rs.getTimestamp("updated_at"))
                        .build();
            }
        }
        return null;
    }

    /**
     * Check if a user exists by email.
     */
    public boolean existsByEmail(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if a user exists by username.
     */
    public boolean existsByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT id, username, password, email FROM users WHERE username = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return User.builder()
                            .id(rs.getInt("id"))
                            .username(rs.getString("username"))
                            .password(rs.getString("password"))
                            .email(rs.getString("email"))
                            .build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertNewUser(String username, String email, String password, int roleId) {
        String sql = """
                WITH new_user AS (
                    INSERT INTO users (username, email, password, role_id, status)
                    VALUES (?, ?, ?, ?, ?)
                    RETURNING id
                )
                INSERT INTO user_images (user_id, image_id)
                SELECT new_user.id, ?
                FROM new_user
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);

            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setInt(4, roleId);
            stmt.setString(5, UserStatus.ACTIVE.name().toLowerCase());
            stmt.setInt(6, DEFAULT_IMAGE_ID);

            int rowsAffected = stmt.executeUpdate();

            conn.commit();

            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // rollback on error
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    public boolean getUserByUsernameAndPassword(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // true if user exists
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
