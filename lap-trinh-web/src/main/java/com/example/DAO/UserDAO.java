package com.example.DAO;

import com.example.Model.*;
import com.example.Service.Database.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private final Connection conn;
    private static final int DEFAULT_IMAGE_ID = 1;

    public UserDAO() {
        this.conn = null;  // code cũ vẫn chạy, sẽ tự tạo connection khi null
    }

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
                      AND u.status = ?
                """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, UserStatus.ACTIVE.getUserStatus());

            try (ResultSet rs = ps.executeQuery()) {
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
                                    ? ImageStatus.fromString(rs.getString("i_status"))
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

                    User user = User.builder()
                            .id(rs.getInt("u_id"))
                            .username(rs.getString("u_username"))
                            .password(rs.getString("u_password"))
                            .fullName(rs.getString("u_full_name"))
                            .email(rs.getString("u_email"))
                            .status(rs.getString("u_status") != null
                                    ? UserStatus.fromString(rs.getString("u_status"))
                                    : null)
                            .roleId(rs.getInt("u_role_id"))
                            .createdAt(rs.getTimestamp("u_created_at"))
                            .updatedAt(rs.getTimestamp("u_updated_at"))
                            .userDetail(userDetail)
                            .userImage(userImage)
                            .build();

                    return user;
                }
            }
        }

        return null;
    }

    /**
     * Check if a user exists by email.
     */
    public boolean existsByEmail(String email) {
        String sql = """
                SELECT id 
                FROM users 
                WHERE email = ?
                  AND u.status = ?
                """;


        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, UserStatus.ACTIVE.getUserStatus());

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
        String sql = """
                SELECT id 
                FROM users u
                WHERE username = ?
                  AND u.status = ?
                """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, UserStatus.ACTIVE.getUserStatus());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserByUsername(String username) {
        String sql = """
                    SELECT 
                        u.id              AS u_id,
                        u.username,
                        u.password,
                        u.full_name,
                        u.email,
                        u.status          AS u_status,
                        u.role_id,
                        u.created_at      AS u_created_at,
                        u.updated_at      AS u_updated_at,
                
                        r.id              AS r_id,
                        r.name            AS r_name,
                        r.status          AS r_status,
                        r.created_at      AS r_created_at,
                        r.updated_at      AS r_updated_at
                    FROM users u
                    JOIN roles r ON u.role_id = r.id
                    WHERE u.username = ?
                      AND u.status = ?
                      AND r.status = ?
                """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, UserStatus.ACTIVE.getUserStatus());
            ps.setString(3, RoleStatus.ACTIVE.getRoleStatus());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Role role = Role.builder()
                            .id(rs.getInt("r_id"))
                            .name(RoleName.fromString(rs.getString("r_name")))
                            .status(RoleStatus.fromString(rs.getString("r_status")))
                            .createdAt(rs.getTimestamp("r_created_at"))
                            .updatedAt(rs.getTimestamp("r_updated_at"))
                            .build();

                    return User.builder()
                            .id(rs.getInt("u_id"))
                            .username(rs.getString("username"))
                            .password(rs.getString("password"))
                            .fullName(rs.getString("full_name"))
                            .email(rs.getString("email"))
                            .status(UserStatus.valueOf(rs.getString("u_status").toUpperCase()))
                            .roleId(rs.getInt("role_id")) // optional, but OK
                            .createdAt(rs.getTimestamp("u_created_at"))
                            .updatedAt(rs.getTimestamp("u_updated_at"))
                            .role(role)
                            .build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean insertNewUser(String username, String email, String password) {
        String sql = """
                WITH role_cte AS (
                    SELECT id AS role_id FROM roles WHERE name = ?
                ),
                new_user AS (
                    INSERT INTO users (username, email, password, role_id, status)
                    SELECT ?, ?, ?, role_id, ?
                    FROM role_cte
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

        Connection conn = null;
        try {
            conn = JDBCConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, RoleName.CUSTOMER.getRoleName());
                stmt.setString(2, username);
                stmt.setString(3, email);
                stmt.setString(4, password);
                stmt.setString(5, UserStatus.ACTIVE.getUserStatus());
                stmt.setInt(6, DEFAULT_IMAGE_ID);

                int rowsAffected = stmt.executeUpdate();
                conn.commit();
                return rowsAffected > 0;
            }

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back due to error in insertNewUser()");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
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

    public boolean getUserByUsernameAndPassword(String username, String password) throws SQLException {
        String sql = """
                SELECT id 
                FROM users 
                WHERE username = ? 
                  AND password = ?
                  AND status = ?
                """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, UserStatus.ACTIVE.getUserStatus());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getHashedPasswordByUsername(String username) throws SQLException {
        String sql = """
                SELECT password 
                FROM users 
                WHERE username = ? 
                  AND status = ?
                """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, UserStatus.ACTIVE.getUserStatus());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password");
                }
            }
        }
        return null;
    }

    public boolean updateUserProfile(
            int userId,
            String fullName,
            String phone,
            String address1,
            String address2,
            String address3,
            String imageUrl
    ) throws SQLException {
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

        Connection conn = null;
        boolean success = false;

        try {
            conn = JDBCConnection.getConnection();
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
                stmt.setString(9, ImageStatus.ACTIVE.getImageStatus());
                stmt.setInt(10, userId);

                System.out.println("Executing updateUserProfile() with params: "
                        + fullName + ", " + phone + ", " + address1 + ", " + address2
                        + ", " + address3 + ", " + userId + ", " + imageUrl);

                int rows = stmt.executeUpdate();

                // Optional sanity check
                if (rows > 0) {
                    conn.commit();
                    success = true;
                } else {
                    conn.rollback();
                    System.err.println("updateUserProfile(): No rows affected — rollback triggered.");
                }
            }

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back due to SQL error in updateUserProfile()");
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();

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

        return success;
    }

    public User getUserById(int userId) throws SQLException {
        String sql = """
                    SELECT *
                    FROM users
                    WHERE id = ?
                      AND status = ?
                """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, UserStatus.ACTIVE.getUserStatus());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return User.builder()
                            .id(rs.getInt("id"))
                            .username(rs.getString("username"))
                            .password(rs.getString("password"))
                            .fullName(rs.getString("full_name"))
                            .email(rs.getString("email"))
                            .status(UserStatus.fromString(rs.getString("status")))
                            .roleId(rs.getInt("role_id"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    public boolean changePassword(String username, String password) throws SQLException {
        String sql = "UPDATE users SET password = ?, updated_at = NOW() WHERE username = ?";

        Connection conn = null;

        try {
            conn = JDBCConnection.getConnection();

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

    public boolean updateFullName(int userId, String fullName) throws SQLException {
        String sql = "UPDATE users SET full_name = ?, updated_at = NOW() WHERE id = ?";

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fullName);
            stmt.setInt(2, userId);

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean updateUserRole(int userID, String username, int roleID) throws SQLException {
        String sql = """
                    UPDATE users
                    SET role_id = ?, updated_at = NOW()
                    WHERE id = ?
                      AND username = ?
                      AND status = ?
                """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, roleID);
            ps.setInt(2, userID);
            ps.setString(3, username);
            ps.setString(4, RoleStatus.ACTIVE.getRoleStatus());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
