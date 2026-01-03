package com.example.DAO;

import com.example.Model.Role;
import com.example.Model.RoleName;
import com.example.Model.RoleStatus;
import com.example.Service.Database.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleDAO {
    public Role getRoleByRoleName(String roleName) throws SQLException {
        String sql = """
                    SELECT
                        id,
                        name,
                        status,
                        created_at,
                        updated_at
                    FROM roles
                    WHERE name = ?
                    LIMIT 1
                """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, roleName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Role.builder()
                            .id(rs.getInt("id"))
                            .name(RoleName.fromString(rs.getString("name")))
                            .status(RoleStatus.fromString(rs.getString("status")))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;

    }
}
