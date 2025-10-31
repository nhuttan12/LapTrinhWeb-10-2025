package com.example.DAO;

import com.example.DTO.Common.PagingMetaData;
import com.example.DTO.Common.PagingResponse;
import com.example.Model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private final Connection conn;

    public ProductDAO(Connection conn) {
        this.conn = conn;
    }

    public PagingResponse<Product> getProductsPaging(PagingMetaData meta, int offset) throws SQLException {
        String sql = """
                    SELECT 
                        p.id as p_id, p.name, p.price, p.discount, p.status as p_status, p.category, 
                        p.created_at as p_created_at, p.updated_at as p_updated_at,
                        pi.id as pi_id, pi.image_id, pi.type as pi_type, pi.created_at as pi_created_at, pi.updated_at as pi_updated_at,
                        i.id as i_id, i.url as i_url, i.status as i_status, i.created_at as i_created_at, i.updated_at as i_updated_at
                    FROM products p
                    LEFT JOIN product_images pi ON pi.product_id = p.id AND pi.type = ?
                    LEFT JOIN images i ON i.id = pi.image_id
                    %s
                    OFFSET ? LIMIT ?
                """;

        List<Product> products = new ArrayList<>();

        try {
            conn.setAutoCommit(false);

            // Build dynamic ORDER BY clause from meta
            String orderByClause = meta.buildOrderByClause("p");
            sql = String.format(sql, orderByClause);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, ImageType.THUMBNAIL.getImageType());
                stmt.setInt(2, offset);
                stmt.setInt(3, meta.getPageSize());

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        // Map Image
                        Image image = null;
                        if (rs.getInt("i_id") != 0) {
                            image = Image.builder()
                                    .id(rs.getInt("i_id"))
                                    .url(rs.getString("i_url"))
                                    .status(ImageStatus.fromString(rs.getString("i_status")))
                                    .createdAt(rs.getTimestamp("i_created_at"))
                                    .updatedAt(rs.getTimestamp("i_updated_at"))
                                    .build();
                        }

                        // Map ProductImage
                        ProductImage productImage = null;
                        if (rs.getInt("pi_id") != 0) {
                            productImage = ProductImage.builder()
                                    .id(rs.getInt("pi_id"))
                                    .imageId(rs.getInt("image_id"))
                                    .productId(rs.getInt("p_id"))
                                    .type(ImageType.fromString(rs.getString("pi_type")))
                                    .createdAt(rs.getTimestamp("pi_created_at"))
                                    .updatedAt(rs.getTimestamp("pi_updated_at"))
                                    .image(image)
                                    .build();
                        }

                        // Map Product
                        Product product = Product.builder()
                                .id(rs.getInt("p_id"))
                                .name(rs.getString("name"))
                                .price(rs.getDouble("price"))
                                .discount(rs.getDouble("discount"))
                                .status(ProductStatus.fromString(rs.getString("p_status")))
                                .category(rs.getString("category"))
                                .createdAt(rs.getTimestamp("p_created_at"))
                                .updatedAt(rs.getTimestamp("p_updated_at"))
                                .productImage(productImage)
                                .build();

                        products.add(product);
                    }
                }
            }

            // Count total items for meta info
            String countSql = "SELECT COUNT(*) AS total FROM products";
            int totalItems = 0;
            try (PreparedStatement countStmt = conn.prepareStatement(countSql);
                 ResultSet countRs = countStmt.executeQuery()) {
                if (countRs.next()) {
                    totalItems = countRs.getInt("total");
                }
            }

            conn.commit();

            // Calculate total pages and navigation flags
            int totalPages = (int) Math.ceil((double) totalItems / meta.getPageSize());
            boolean hasNext = meta.getCurrentPage() < totalPages;
            boolean hasPrevious = meta.getCurrentPage() > 1;

            PagingMetaData updatedMeta = PagingMetaData.builder()
                    .currentPage(meta.getCurrentPage())
                    .pageSize(meta.getPageSize())
                    .totalItems(totalItems)
                    .totalPages(totalPages)
                    .hasNext(hasNext)
                    .hasPrevious(hasPrevious)
                    .sortBy(meta.getSortBy())
                    .sortDirections(meta.getSortDirections())
                    .build();

            return PagingResponse.<Product>builder()
                    .items(products)
                    .meta(updatedMeta)
                    .build();

        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public PagingResponse<Product> getNewProductsPaging(PagingMetaData meta, int offset) throws SQLException {
        List<Product> products = new ArrayList<>();

        String sql = """
                    SELECT 
                        p.id as p_id, p.name, p.price, p.discount, p.status as p_status, p.category, 
                        p.created_at as p_created_at, p.updated_at as p_updated_at,
                        pi.id as pi_id, pi.image_id, pi.type as pi_type, pi.created_at as pi_created_at, pi.updated_at as pi_updated_at,
                        i.id as i_id, i.url as i_url, i.status as i_status, i.created_at as i_created_at, i.updated_at as i_updated_at
                    FROM products p
                    LEFT JOIN product_images pi ON pi.product_id = p.id AND pi.type = ?
                    LEFT JOIN images i ON i.id = pi.image_id
                    ORDER BY p.created_at DESC
                    OFFSET ? LIMIT ?
                """;

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, ImageType.THUMBNAIL.getImageType());
                stmt.setInt(2, offset);
                stmt.setInt(3, meta.getPageSize());

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        // Map Image
                        Image image = null;
                        if (rs.getInt("i_id") != 0) {
                            image = Image.builder()
                                    .id(rs.getInt("i_id"))
                                    .url(rs.getString("i_url"))
                                    .status(ImageStatus.fromString(rs.getString("i_status")))
                                    .createdAt(rs.getTimestamp("i_created_at"))
                                    .updatedAt(rs.getTimestamp("i_updated_at"))
                                    .build();
                        }

                        // Map ProductImage
                        ProductImage productImage = null;
                        if (rs.getInt("pi_id") != 0) {
                            productImage = ProductImage.builder()
                                    .id(rs.getInt("pi_id"))
                                    .imageId(rs.getInt("image_id"))
                                    .productId(rs.getInt("p_id"))
                                    .type(ImageType.fromString(rs.getString("pi_type")))
                                    .createdAt(rs.getTimestamp("pi_created_at"))
                                    .updatedAt(rs.getTimestamp("pi_updated_at"))
                                    .image(image)
                                    .build();
                        }

                        // Map Product
                        Product product = Product.builder()
                                .id(rs.getInt("p_id"))
                                .name(rs.getString("name"))
                                .price(rs.getDouble("price"))
                                .discount(rs.getDouble("discount"))
                                .status(ProductStatus.fromString(rs.getString("p_status")))
                                .category(rs.getString("category"))
                                .createdAt(rs.getTimestamp("p_created_at"))
                                .updatedAt(rs.getTimestamp("p_updated_at"))
                                .productImage(productImage)
                                .build();

                        products.add(product);
                    }
                }

                // Count total items for meta info
                String countSql = "SELECT COUNT(*) AS total FROM products";
                int totalItems = 0;
                try (PreparedStatement countStmt = conn.prepareStatement(countSql);
                     ResultSet countRs = countStmt.executeQuery()) {
                    if (countRs.next()) {
                        totalItems = countRs.getInt("total");
                    }
                }

                conn.commit();

                // Calculate total pages and navigation flags
                int totalPages = (int) Math.ceil((double) totalItems / meta.getPageSize());
                boolean hasNext = meta.getCurrentPage() < totalPages;
                boolean hasPrevious = meta.getCurrentPage() > 1;

                PagingMetaData updatedMeta = PagingMetaData.builder()
                        .currentPage(meta.getCurrentPage())
                        .pageSize(meta.getPageSize())
                        .totalItems(totalItems)
                        .totalPages(totalPages)
                        .hasNext(hasNext)
                        .hasPrevious(hasPrevious)
                        .sortBy(meta.getSortBy())
                        .sortDirections(meta.getSortDirections())
                        .build();

                return PagingResponse.<Product>builder()
                        .items(products)
                        .meta(updatedMeta)
                        .build();

            }
        } catch (SQLException ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(true);
        }
    }
}
