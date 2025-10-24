package com.example.DAO;

import com.example.Model.*;

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
        String sql = """
                    SELECT
                        u.id AS u_id, u.username AS u_username, u.password AS u_password,
                        u.full_name AS u_full_name, u.email AS u_email, u.status AS u_status,
                        u.role_id AS u_role_id, u.created_at AS u_created_at, u.updated_at AS u_updated_at,
                
                        ud.id AS ud_id, ud.phone AS ud_phone, ud.user_id AS ud_user_id,
                        ud.address_1 AS ud_address_1, ud.address_2 AS ud_address_2, ud.address_3 AS ud_address_3,
                        ud.created_at AS ud_created_at, ud.updated_at AS ud_updated_at,
                
                        ui.id AS ui_id, ui.user_id AS ui_user_id, ui.image_id AS ui_image_id,
                        ui.created_at AS ui_created_at, ui.updated_at AS ui_updated_at,
                
                        i.id AS i_id, i.url AS i_url, i.status AS i_status,
                        i.created_at AS i_created_at, i.updated_at AS i_updated_at
                    FROM users u
                    LEFT JOIN user_details ud ON u.id = ud.user_id
                    LEFT JOIN LATERAL (
                        SELECT *
                        FROM user_images
                        WHERE user_id = u.id
                        ORDER BY created_at DESC
                        LIMIT 1
                    ) ui ON TRUE
                    LEFT JOIN images i ON ui.image_id = i.id
                    WHERE u.id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                UserDetail userDetail = UserDetail.builder()
                        .id(rs.getInt("ud_id"))
                        .userId(rs.getInt("ud_user_id"))
                        .phone(rs.getString("ud_phone"))
                        .address1(rs.getString("ud_address_1"))
                        .address2(rs.getString("ud_address_2"))
                        .address3(rs.getString("ud_address_3"))
                        .createdAt(rs.getTimestamp("ud_created_at"))
                        .updatedAt(rs.getTimestamp("ud_updated_at"))
                        .build();

                Image image = Image.builder()
                        .id(rs.getInt("i_id"))
                        .url(rs.getString("i_url"))
                        .status(rs.getString("i_status") != null
                                ? ImageStatus.valueOf(rs.getString("i_status").toUpperCase())
                                : null)
                        .createdAt(rs.getTimestamp("i_created_at"))
                        .updatedAt(rs.getTimestamp("i_updated_at"))
                        .build();

                UserImage userImage = UserImage.builder()
                        .id(rs.getInt("ui_id"))
                        .userId(rs.getInt("ui_user_id"))
                        .imageId(rs.getInt("ui_image_id"))
                        .createdAt(rs.getTimestamp("ui_created_at"))
                        .updatedAt(rs.getTimestamp("ui_updated_at"))
                        .image(image)
                        .build();

                return User.builder()
                        .id(rs.getInt("u_id"))
                        .username(rs.getString("u_username"))
                        .password(rs.getString("u_password"))
                        .fullName(rs.getString("u_full_name"))
                        .email(rs.getString("u_email"))
                        .status(rs.getString("u_status") != null
                                ? UserStatus.valueOf(rs.getString("u_status").toUpperCase())
                                : null)
                        .roleId(rs.getInt("u_role_id"))
                        .createdAt(rs.getTimestamp("u_created_at"))
                        .updatedAt(rs.getTimestamp("u_updated_at"))
                        .userDetail(userDetail)
                        .userImage(userImage)
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
                ),
                inserted_details AS (
                    INSERT INTO user_details (user_id, phone, address_1, address_2, address_3)
                    SELECT id, '', '', '', ''
                    FROM new_user
                )
                INSERT INTO user_images (user_id, image_id)
                SELECT id, ?
                FROM new_user;
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
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public boolean getUserByUsernameAndPassword(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ? AND status = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, UserStatus.ACTIVE.name().toLowerCase());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserProfile(
            int userId,
            String fullName,
            String phone,
            String address1,
            String address2,
            String address3,
            String imageUrl
    ) {
        String updateSQL = """
                    WITH updated_user AS (
                        UPDATE users
                        SET full_name = ?, updated_at = NOW()
                        WHERE id = ?
                        RETURNING id
                    ),
                    updated_details AS (
                        UPDATE user_details
                        SET phone = ?, address_1 = ?, address_2 = ?, address_3 = ?, updated_at = NOW()
                        WHERE user_id = ?
                        RETURNING user_id
                    ),
                    inserted_image AS (
                        INSERT INTO images (url, status, created_at, updated_at)
                        VALUES (?, ?, NOW(), NOW())
                        RETURNING id
                    )
                    INSERT INTO user_images (user_id, image_id, created_at, updated_at)
                    SELECT ?, ii.id, NOW(), NOW()
                    FROM updated_details ud, inserted_image ii;
                """;

        boolean success = false;

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(updateSQL)) {
                stmt.setString(1, fullName);
                stmt.setInt(2, userId);
                stmt.setString(3, phone);
                stmt.setString(4, address1);
                stmt.setString(5, address2);
                stmt.setString(6, address3);
                stmt.setInt(7, userId);
                stmt.setString(8, imageUrl);
                stmt.setString(9, ImageStatus.ACTIVE.name().toLowerCase());
                stmt.setInt(10, userId);

                stmt.executeUpdate();
            }

            conn.commit();
            success = true;
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return success;
    }

    public User getUserById(int userId) {
        String sql = """
                    SELECT *
                    FROM users
                    WHERE id = ?
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return User.builder()
                            .id(rs.getInt("id"))
                            .username(rs.getString("username"))
                            .password(rs.getString("password"))
                            .fullName(rs.getString("full_name"))
                            .email(rs.getString("email"))
                            .status(UserStatus.valueOf(rs.getString("status").toUpperCase()))
                            .roleId(rs.getInt("role_id"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build();
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean changePassword(String username, String password) {
        String sql = "UPDATE users SET password = ? WHERE username = ?";

        try {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, password);
                stmt.setString(2, username);

                int rowsUpdated = stmt.executeUpdate();

                if (rowsUpdated > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    return false;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
