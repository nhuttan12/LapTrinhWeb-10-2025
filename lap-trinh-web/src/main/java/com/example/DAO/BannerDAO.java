package com.example.DAO;

import com.example.Model.Image;
import com.example.Model.ImageStatus;
import com.example.Model.ImageType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BannerDAO {

    private final Connection connection;

    // Constructor nhận connection từ pool
    public BannerDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection không được null");
        }
        this.connection = connection;
    }

    // Lấy tất cả banner đang active
    public List<Image> getAllBanners() throws SQLException {
        String sql = "SELECT * FROM images WHERE type = ? AND status = 'active'";
        List<Image> banners = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ImageType.BANNER.getImageType()); // chuyển enum sang string
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Image image = Image.builder()
                        .id(rs.getInt("id"))
                        .url(rs.getString("url"))
                        .status(ImageStatus.fromString(rs.getString("status"))) // chuyển string DB sang enum
                        .type(ImageType.fromString(rs.getString("type")))      // chuyển string DB sang enum
                        .createdAt(rs.getTimestamp("created_at"))
                        .updatedAt(rs.getTimestamp("updated_at"))
                        .build();

                banners.add(image);
            }
        }

        return banners;
    }

    // Thêm banner mới
    public boolean addBanner(String url) throws SQLException {
        String sql = "INSERT INTO images (url, type, status, created_at, updated_at) " +
                "VALUES (?, ?, 'active', NOW(), NOW())";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, url);
            stmt.setString(2, ImageType.BANNER.getImageType());
            return stmt.executeUpdate() > 0;
        }
    }

    // Xóa banner (chỉ disable, không xóa thật)
    public boolean deleteBanner(int id) throws SQLException {
        String sql = "UPDATE images SET status = 'inactive', updated_at = NOW() " +
                "WHERE id = ? AND type = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setString(2, ImageType.BANNER.getImageType());
            return stmt.executeUpdate() > 0;
        }
    }
}
