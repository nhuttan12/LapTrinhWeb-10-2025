package com.example.DAO;

import com.example.Model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminProductDAO {
    private final Connection conn;

    public AdminProductDAO(Connection conn) {
        this.conn = conn;
    }

    // ✅ 1. Lấy danh sách sản phẩm với phân trang
    public List<Product> getAllProductsPaginated(int offset, int limit) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = """
            SELECT p.id AS product_id, p.name, p.price, p.discount, p.status, p.category,
                   p.created_at AS product_created_at, p.updated_at AS product_updated_at,
                   pd.id AS detail_id, pd.description,
                   pi.id AS image_id, i.url AS image_url
            FROM products p
            LEFT JOIN product_details pd ON p.id = pd.product_id
            LEFT JOIN product_images pi ON p.id = pi.product_id AND pi.type = 'THUMBNAIL'
            LEFT JOIN images i ON pi.image_id = i.id
            ORDER BY p.id ASC
            LIMIT ? OFFSET ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product product = Product.builder()
                            .id(rs.getInt("product_id"))
                            .name(rs.getString("name"))
                            .price(rs.getDouble("price"))
                            .discount(rs.getDouble("discount"))
                            .status(ProductStatus.fromString(rs.getString("status")))
                            .category(rs.getString("category"))
                            .createdAt(rs.getTimestamp("product_created_at"))
                            .updatedAt(rs.getTimestamp("product_updated_at"))
                            .productDetail(ProductDetail.builder()
                                    .id(rs.getInt("detail_id"))
                                    .description(rs.getString("description"))
                                    .build())
                            .productImage(ProductImage.builder()
                                    .id(rs.getInt("image_id"))
                                    .image(Image.builder()
                                            .url(rs.getString("image_url"))
                                            .build())
                                    .build())
                            .build();
                    products.add(product);
                }
            }
        }
        return products;
    }

    // ✅ 2. Lấy sản phẩm theo ID đầy đủ
    public Product getProductById(int id) throws SQLException {
        String sql = """
            SELECT p.id AS product_id, p.name, p.price, p.discount, p.status, p.category,
                   p.created_at AS product_created_at, p.updated_at AS product_updated_at,
                   pd.id AS detail_id, pd.description,
                   pi.id AS image_id, i.url AS image_url
            FROM products p
            LEFT JOIN product_details pd ON p.id = pd.product_id
            LEFT JOIN product_images pi ON p.id = pi.product_id AND pi.type = 'THUMBNAIL'
            LEFT JOIN images i ON pi.image_id = i.id
            WHERE p.id = ?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Product.builder()
                            .id(rs.getInt("product_id"))
                            .name(rs.getString("name"))
                            .price(rs.getDouble("price"))
                            .discount(rs.getDouble("discount"))
                            .status(ProductStatus.fromString(rs.getString("status")))
                            .category(rs.getString("category"))
                            .createdAt(rs.getTimestamp("product_created_at"))
                            .updatedAt(rs.getTimestamp("product_updated_at"))
                            .productDetail(ProductDetail.builder()
                                    .id(rs.getInt("detail_id"))
                                    .description(rs.getString("description"))
                                    .build())
                            .productImage(ProductImage.builder()
                                    .id(rs.getInt("image_id"))
                                    .image(Image.builder()
                                            .url(rs.getString("image_url"))
                                            .build())
                                    .build())
                            .build();
                }
            }
        }
        return null;
    }

    // ✅ 3. Tạo mới sản phẩm + detail + thumbnail
    public boolean createProduct(Product product) throws SQLException {
        conn.setAutoCommit(false);
        try {
            // 3.1 Insert vào bảng products
            String sqlProduct = "INSERT INTO products (name, price, discount, status, category, created_at, updated_at) VALUES (?, ?, ?, ?, ?, NOW(), NOW())";
            int productId;
            try (PreparedStatement ps = conn.prepareStatement(sqlProduct, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, product.getName());
                ps.setDouble(2, product.getPrice());
                ps.setDouble(3, product.getDiscount());
                ps.setString(4, product.getStatus().getProductStatus());
                ps.setString(5, product.getCategory());
                ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) productId = rs.getInt(1);
                    else throw new SQLException("Failed to get generated product ID");
                }
            }

            // 3.2 Insert vào bảng product_details
            if (product.getProductDetail() != null) {
                String sqlDetail = "INSERT INTO product_details (product_id, description) VALUES (?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sqlDetail)) {
                    ps.setInt(1, productId);
                    ps.setString(2, product.getProductDetail().getDescription());
                    ps.executeUpdate();
                }
            }

            // 3.3 Insert vào bảng product_images + images nếu cần
            if (product.getProductImage() != null && product.getProductImage().getImage() != null) {
                String sqlImage = "INSERT INTO images (url, created_at) VALUES (?, NOW())";
                int imageId;
                try (PreparedStatement ps = conn.prepareStatement(sqlImage, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, product.getProductImage().getImage().getUrl());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) imageId = rs.getInt(1);
                        else throw new SQLException("Failed to get generated image ID");
                    }
                }

                String sqlProductImage = "INSERT INTO product_images (product_id, image_id, type, created_at) VALUES (?, ?, 'THUMBNAIL', NOW())";
                try (PreparedStatement ps = conn.prepareStatement(sqlProductImage)) {
                    ps.setInt(1, productId);
                    ps.setInt(2, imageId);
                    ps.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // ✅ 4. Cập nhật sản phẩm + detail + thumbnail
    public boolean updateProduct(Product product) throws SQLException {
        conn.setAutoCommit(false);
        try {
            // 4.1 Update bảng products
            String sqlProduct = "UPDATE products SET name = ?, price = ?, discount = ?, status = ?, category = ?, updated_at = NOW() WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sqlProduct)) {
                ps.setString(1, product.getName());
                ps.setDouble(2, product.getPrice());
                ps.setDouble(3, product.getDiscount());
                ps.setString(4, product.getStatus().getProductStatus());
                ps.setString(5, product.getCategory());
                ps.setInt(6, product.getId());
                ps.executeUpdate();
            }

            // 4.2 Update product_details
            if (product.getProductDetail() != null) {
                String sqlDetail = "UPDATE product_details SET description = ? WHERE product_id = ?";
                try (PreparedStatement ps = conn.prepareStatement(sqlDetail)) {
                    ps.setString(1, product.getProductDetail().getDescription());
                    ps.setInt(2, product.getId());
                    ps.executeUpdate();
                }
            }

            // 4.3 Update product_images + images (thumbnail)
            if (product.getProductImage() != null && product.getProductImage().getImage() != null) {
                // 4.3.1 Lấy image_id cũ
                Integer imageId = null;
                String sqlSelect = "SELECT image_id FROM product_images WHERE product_id = ? AND type = 'THUMBNAIL'";
                try (PreparedStatement ps = conn.prepareStatement(sqlSelect)) {
                    ps.setInt(1, product.getId());
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) imageId = rs.getInt("image_id");
                    }
                }

                if (imageId != null) {
                    // Update url
                    String sqlUpdate = "UPDATE images SET url = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                        ps.setString(1, product.getProductImage().getImage().getUrl());
                        ps.setInt(2, imageId);
                        ps.executeUpdate();
                    }
                } else {
                    // Chưa có thumbnail → insert mới
                    String sqlInsertImage = "INSERT INTO images (url, created_at) VALUES (?, NOW())";
                    int newImageId;
                    try (PreparedStatement ps = conn.prepareStatement(sqlInsertImage, PreparedStatement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, product.getProductImage().getImage().getUrl());
                        ps.executeUpdate();
                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            if (rs.next()) newImageId = rs.getInt(1);
                            else throw new SQLException("Failed to get generated image ID");
                        }
                    }
                    String sqlInsertProductImage = "INSERT INTO product_images (product_id, image_id, type, created_at) VALUES (?, ?, 'THUMBNAIL', NOW())";
                    try (PreparedStatement ps = conn.prepareStatement(sqlInsertProductImage)) {
                        ps.setInt(1, product.getId());
                        ps.setInt(2, newImageId);
                        ps.executeUpdate();
                    }
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    // ✅ 5. Soft delete
    public boolean softRemoveProduct(int id) throws SQLException {
        String sql = "UPDATE products SET status = 'inactive', updated_at = NOW() WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
