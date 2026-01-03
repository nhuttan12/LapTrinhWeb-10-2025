package com.example.DAO;

import com.example.DTO.Common.PagingMetaData;
import com.example.DTO.Common.PagingResponse;
import com.example.Model.Banner;
import com.example.Model.Image;
import com.example.Model.ImageStatus;
import com.example.Model.ImageType;
import com.example.Service.Database.JDBCConnection;
import com.example.Utils.BuildOrderPaging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BannerDAO {

    private final Connection connection;
    private final BuildOrderPaging buildOrderPaging;

    // Constructor nhận connection từ pool
    public BannerDAO(Connection connection) {
        if (connection == null) {
            throw new IllegalArgumentException("Connection không được null");
        }
        this.connection = connection;
        this.buildOrderPaging = new BuildOrderPaging();
    }

    // Lấy tất cả banner đang active
    public PagingResponse<Banner> getBannersPaging(
            int offset,
            int currentPage,
            int pageSize) throws SQLException {
        String sql = """
                    SELECT
                        b.id               AS banner_id,
                        b.created_at       AS banner_created_at,
                        b.updated_at       AS banner_updated_at,
                
                        i.id               AS image_id,
                        i.url,
                        i.status,
                        i.created_at       AS image_created_at,
                        i.updated_at       AS image_updated_at
                    FROM banners b
                    JOIN images i ON b.image_id = i.id
                    WHERE i.status = ?
                    ORDER BY b.created_at DESC
                    OFFSET ? LIMIT ?
                """;


        List<Banner> banners = new ArrayList<>();

        try (Connection conn = JDBCConnection.getConnection()) {

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, ImageStatus.ACTIVE.getImageStatus());
                ps.setInt(2, offset);
                ps.setInt(3, pageSize);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {

                        Image image = Image.builder()
                                .id(rs.getInt("image_id"))
                                .url(rs.getString("url"))
                                .status(
                                        ImageStatus.valueOf(
                                                rs.getString("status").toUpperCase()
                                        )
                                )
                                .createdAt(rs.getTimestamp("image_created_at"))
                                .updatedAt(rs.getTimestamp("image_updated_at"))
                                .build();

                        Banner banner = Banner.builder()
                                .id(rs.getInt("banner_id"))
                                .image(image)
                                .createdAt(rs.getTimestamp("banner_created_at"))
                                .updatedAt(rs.getTimestamp("banner_updated_at"))
                                .build();

                        banners.add(banner);
                    }
                }
            }

            // Count total items for meta info
            String countSql = """
                        SELECT COUNT(*) AS total
                        FROM banners b
                        JOIN images i ON b.image_id = i.id
                        WHERE i.status = ?
                    """;

            int totalItems = 0;
            try (PreparedStatement ps = conn.prepareStatement(countSql)) {
                ps.setString(1, ImageStatus.ACTIVE.getImageStatus());
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        totalItems = rs.getInt("total");
                    }
                }
            }

            int totalPages = (int) Math.ceil((double) totalItems / pageSize);

            PagingMetaData meta = PagingMetaData.builder()
                    .currentPage(currentPage)
                    .pageSize(pageSize)
                    .totalItems(totalItems)
                    .totalPages(totalPages)
                    .hasNext(currentPage < totalPages)
                    .hasPrevious(currentPage > 1)
                    .build();

            return PagingResponse.<Banner>builder()
                    .items(banners)
                    .meta(meta)
                    .build();
        }
    }

    // Thêm banner mới
    public boolean addBanner(String url) throws SQLException {
        String insertImageSql =
                "INSERT INTO images (url, status, created_at, updated_at) " +
                        "VALUES (?, ?, NOW(), NOW())";

        String insertBannerSql =
                "INSERT INTO banners (image_id, created_at, updated_at) " +
                        "VALUES (?, NOW(), NOW())";

        connection.setAutoCommit(false);

        try (
                PreparedStatement imageStmt =
                        connection.prepareStatement(insertImageSql, PreparedStatement.RETURN_GENERATED_KEYS);
        ) {
            // 1. Insert image
            imageStmt.setString(1, url);
            imageStmt.setString(2, ImageStatus.ACTIVE.getImageStatus());
            int affectedRows = imageStmt.executeUpdate();

            if (affectedRows == 0) {
                connection.rollback();
                return false;
            }

            // 2. Get generated image ID
            int imageId;
            try (var rs = imageStmt.getGeneratedKeys()) {
                if (!rs.next()) {
                    connection.rollback();
                    return false;
                }
                imageId = rs.getInt(1);
            }

            // 3. Insert banner
            try (PreparedStatement bannerStmt = connection.prepareStatement(insertBannerSql)) {
                bannerStmt.setInt(1, imageId);
                bannerStmt.executeUpdate();
            }

            connection.commit();
            return true;

        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

    // Xóa banner (chỉ disable, không xóa thật)
    public boolean deleteBanner(int id) throws SQLException {
        String sql = "UPDATE images SET status = ?, updated_at = NOW() " +
                "WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ImageStatus.INACTIVE.getImageStatus());
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        }
    }
}
