package com.example.DAO;

import com.example.Model.UserDetail;
import com.example.Service.Database.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDetailDAO {
    private final Connection conn;

    public UserDetailDAO() {
        this.conn = null; // sẽ lấy connection mặc định bên JDBCConnection
    }

    public UserDetailDAO(Connection conn) {
        this.conn = conn;
    }

    private Connection getConnection() throws SQLException {
        return (conn != null) ? conn : JDBCConnection.getConnection();
    }
    public UserDetail getByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM user_details WHERE user_id = ?";

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return UserDetail.builder()
                            .id(rs.getInt("id"))
                            .userId(rs.getInt("user_id"))
                            .phone(rs.getString("phone"))
                            .address1(rs.getString("address_1"))
                            .address2(rs.getString("address_2"))
                            .address3(rs.getString("address_3"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build();
                }
            }
        }

        return null;
    }

    public boolean insert(UserDetail detail) throws SQLException {
        String sql = """
            INSERT INTO user_details (user_id, phone, address_1, address_2, address_3, created_at, updated_at)
            VALUES (?, ?, ?, ?, ?, NOW(), NOW())
        """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, detail.getUserId());
            stmt.setString(2, detail.getPhone());
            stmt.setString(3, detail.getAddress1());
            stmt.setString(4, detail.getAddress2());
            stmt.setString(5, detail.getAddress3());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean update(UserDetail detail) throws SQLException {
        String sql = """
            UPDATE user_details
            SET phone = ?, address_1 = ?, address_2 = ?, address_3 = ?, updated_at = NOW()
            WHERE user_id = ?
        """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, detail.getPhone());
            stmt.setString(2, detail.getAddress1());
            stmt.setString(3, detail.getAddress2());
            stmt.setString(4, detail.getAddress3());
            stmt.setInt(5, detail.getUserId());

            return stmt.executeUpdate() > 0;
        }
    }
}
