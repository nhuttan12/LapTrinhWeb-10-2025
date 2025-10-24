package com.example.DAO;

import com.example.Model.ImageStatus;
import com.example.Model.RoleName;
import com.example.Model.RoleStatus;
import com.example.Service.Database.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DatabaseSeeder {

    private static final String DEFAULT_IMAGE_PATH = "src/main/resources/user-circle-isolated-icon-round-600nw-2459622791.webp";

    public static void main(String[] args) {
        try (Connection conn = JDBCConnection.getConnection()) {

            /*
             * Seeding roles
             */
            int id = 1;
            for (RoleName role : RoleName.values()) {
                if (!roleExists(conn, id)) {
                    insertRole(conn, id, role);
                    System.out.println("Inserted role: " + role.name());
                } else {
                    System.out.println("Role already exists: " + role.name());
                }
                id++;
            }

            /*
             * Seed default image
             */
            int defaultImageId = 1;
            if (!imageExists(conn, defaultImageId)) {
                insertImage(conn, defaultImageId, DEFAULT_IMAGE_PATH);
                System.out.println("Inserted default user image.");
            } else {
                System.out.println("Default image already exists.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean roleExists(Connection conn, int roleId) throws Exception {
        String sql = "SELECT COUNT(*) FROM roles WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private static void insertRole(Connection conn, int roleId, RoleName roleName) throws Exception {
        String sql = "INSERT INTO roles (id, name, status) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, roleId);
            stmt.setString(2, roleName.name());
            stmt.setString(3, RoleStatus.ACTIVE.name().toLowerCase());
            stmt.executeUpdate();
        }
    }

    private static boolean imageExists(Connection conn, int imageId) throws Exception {
        String sql = "SELECT COUNT(*) FROM images WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, imageId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    private static void insertImage(Connection conn, int imageId, String url) throws Exception {
        String sql = "INSERT INTO images (id, url, status) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, imageId);
            stmt.setString(2, url);
            stmt.setString(3, ImageStatus.ACTIVE.name().toLowerCase());
            stmt.executeUpdate();
        }
    }

}
