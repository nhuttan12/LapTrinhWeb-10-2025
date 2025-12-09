package com.example.DAO;

import com.example.DTO.Products.GetProductsPagingResponseDTO;
import com.example.Model.*;

import java.sql.*;
import java.util.*;

public class AdminProductDAO {
    private final Connection conn;

    public AdminProductDAO(Connection conn) {
        this.conn = conn;
    }

    // expose connection để transaction ở service
    public Connection getConnection() {
        return this.conn;
    }

    //  Lấy danh sách sản phẩm kèm brand + thumbnail
    public List<GetProductsPagingResponseDTO> getProductsPaging(int offset, int limit) throws SQLException {
        List<GetProductsPagingResponseDTO> products = new ArrayList<>();

        String sql = """
    SELECT 
        p.id AS product_id,
        p.name AS product_name,
        p.price,
        p.discount,
        b.name AS brand_name,
        i.url AS thumbnail_url
    FROM products p
    LEFT JOIN product_details pd ON p.id = pd.product_id
    LEFT JOIN brands b ON pd.brand_id = b.id
    LEFT JOIN product_images pi 
           ON pi.product_id = p.id AND UPPER(pi.type) = 'THUMBNAIL'
    LEFT JOIN images i ON pi.image_id = i.id
    WHERE p.status = 'active'
    ORDER BY p.id DESC
    LIMIT ? OFFSET ?
""";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setInt(2, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    GetProductsPagingResponseDTO dto = new GetProductsPagingResponseDTO();
                    dto.setId(rs.getInt("product_id"));
                    dto.setName(rs.getString("product_name"));
                    dto.setPrice(rs.getDouble("price"));
                    dto.setDiscount(rs.getDouble("discount"));
                    dto.setBrand(rs.getString("brand_name") != null ? rs.getString("brand_name") : "Không rõ");

                    String thumbnail = rs.getString("thumbnail_url");
                    dto.setThumbnail(thumbnail != null ? thumbnail : ""); // tránh null gây lỗi JSP

                    products.add(dto);
                }
            }
        }
        return products;
    }

    public int countAllProducts() throws SQLException {
        String sql = "SELECT COUNT(*) FROM products WHERE status = 'active'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        }
        return 0;
    }

    // Create Product
    public int insertProduct(Product product) throws SQLException {
        String sql = """
            INSERT INTO products (name, price, discount, status, created_at)
            VALUES (?, ?, ?, ?, NOW())
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setDouble(3, product.getDiscount());
            ps.setString(4, product.getStatus().name());
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            throw new SQLException("Cannot get generated product id");
        }
    }

    public void insertProductDetail(ProductDetail detail) throws SQLException {
        String sql = """
            INSERT INTO product_details (
                product_id, brand_id, os, ram, storage, battery_capacity,
                screen_size, screen_resolution, mobile_network, cpu, gpu,
                water_resistance, max_charge_watt, design, memory_card,
                cpu_speed, release_date, rating, description, created_at
            ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW())
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int k = 1;
            ps.setInt(k++, detail.getProductId());

            // brand may be null
            if (detail.getBrand() != null) ps.setInt(k++, detail.getBrand().getId());
            else ps.setNull(k++, Types.INTEGER);

            ps.setString(k++, detail.getOs());
            if (detail.getRam() != null) ps.setInt(k++, detail.getRam()); else ps.setNull(k++, Types.INTEGER);
            if (detail.getStorage() != null) ps.setInt(k++, detail.getStorage()); else ps.setNull(k++, Types.INTEGER);
            if (detail.getBatteryCapacity() != null) ps.setInt(k++, detail.getBatteryCapacity()); else ps.setNull(k++, Types.INTEGER);
            if (detail.getScreenSize() != null) ps.setDouble(k++, detail.getScreenSize()); else ps.setNull(k++, Types.DOUBLE);
            ps.setString(k++, detail.getScreenResolution());
            ps.setString(k++, detail.getMobileNetwork());
            ps.setString(k++, detail.getCpu());
            ps.setString(k++, detail.getGpu());
            ps.setString(k++, detail.getWaterResistance());
            if (detail.getMaxChargeWatt() != null) ps.setInt(k++, detail.getMaxChargeWatt()); else ps.setNull(k++, Types.INTEGER);
            ps.setString(k++, detail.getDesign());
            ps.setString(k++, detail.getMemoryCard());
            if (detail.getCpuSpeed() != null) ps.setDouble(k++, detail.getCpuSpeed()); else ps.setNull(k++, Types.DOUBLE);
            ps.setTimestamp(k++, detail.getReleaseDate());
            if (detail.getRating() != null) ps.setDouble(k++, detail.getRating()); else ps.setNull(k++, Types.DOUBLE);
            ps.setString(k++, detail.getDescription());
            ps.executeUpdate();
        }
    }

    public int insertImage(String url) throws SQLException {
        String sql = "INSERT INTO images (url, created_at) VALUES (?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, url);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            throw new SQLException("Cannot get generated image id");
        }
    }

    public void insertProductImage(int productId, int imageId, String type) throws SQLException {
        String sql = """
        INSERT INTO product_images (product_id, image_id, type, created_at)
        VALUES (?, ?, ?, NOW())
    """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setInt(2, imageId);
            ps.setString(3, type != null ? type.toUpperCase() : null);
            ps.executeUpdate();
        }
    }

    // delete product_images by product and type (useful when replacing thumbnail)
    public void deleteProductImagesByType(int productId, String type) throws SQLException {
        String sql = "DELETE FROM product_images WHERE product_id = ? AND UPPER(type) = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.setString(2, type.toUpperCase());
            ps.executeUpdate();
        }
    }

    // Update Products
    public void updateProduct(Product product) throws SQLException {
        String sql = """
            UPDATE products
            SET name=?, price=?, discount=?, status=?, updated_at=NOW()
            WHERE id=?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice());
            ps.setDouble(3, product.getDiscount());
            ps.setString(4, product.getStatus().name());
            ps.setInt(5, product.getId());
            ps.executeUpdate();
        }
    }

    public void updateProductDetail(ProductDetail detail) throws SQLException {
        String sql = """
            UPDATE product_details
            SET brand_id=?, os=?, ram=?, storage=?, battery_capacity=?,
                screen_size=?, screen_resolution=?, mobile_network=?, cpu=?, gpu=?,
                water_resistance=?, max_charge_watt=?, design=?, memory_card=?,
                cpu_speed=?, release_date=?, rating=?, description=?, updated_at=NOW()
            WHERE product_id=?
        """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int k = 1;
            if (detail.getBrand() != null) ps.setInt(k++, detail.getBrand().getId()); else ps.setNull(k++, Types.INTEGER);
            ps.setString(k++, detail.getOs());
            if (detail.getRam() != null) ps.setInt(k++, detail.getRam()); else ps.setNull(k++, Types.INTEGER);
            if (detail.getStorage() != null) ps.setInt(k++, detail.getStorage()); else ps.setNull(k++, Types.INTEGER);
            if (detail.getBatteryCapacity() != null) ps.setInt(k++, detail.getBatteryCapacity()); else ps.setNull(k++, Types.INTEGER);
            if (detail.getScreenSize() != null) ps.setDouble(k++, detail.getScreenSize()); else ps.setNull(k++, Types.DOUBLE);
            ps.setString(k++, detail.getScreenResolution());
            ps.setString(k++, detail.getMobileNetwork());
            ps.setString(k++, detail.getCpu());
            ps.setString(k++, detail.getGpu());
            ps.setString(k++, detail.getWaterResistance());
            if (detail.getMaxChargeWatt() != null) ps.setInt(k++, detail.getMaxChargeWatt()); else ps.setNull(k++, Types.INTEGER);
            ps.setString(k++, detail.getDesign());
            ps.setString(k++, detail.getMemoryCard());
            if (detail.getCpuSpeed() != null) ps.setDouble(k++, detail.getCpuSpeed()); else ps.setNull(k++, Types.DOUBLE);
            ps.setTimestamp(k++, detail.getReleaseDate());
            if (detail.getRating() != null) ps.setDouble(k++, detail.getRating()); else ps.setNull(k++, Types.DOUBLE);
            ps.setString(k++, detail.getDescription());
            ps.setInt(k++, detail.getProductId());
            ps.executeUpdate();
        }
    }

    // Delete all product_images by productId (existing method renamed for clarity)
    public void deleteImagesByProductId(int productId) throws SQLException {
        String sql = "DELETE FROM product_images WHERE product_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ps.executeUpdate();
        }
    }

    /* SOFT DELETE */
    public void softDelete(int id) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "UPDATE products SET status='inactive', updated_at=NOW() WHERE id=?"
        )) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ========== New: get product by id ==========
    public Product getProductById(int id) throws SQLException {
//        String sql = "SELECT id, name, price, discount, status FROM products WHERE id = ?";
        String sql = """
        SELECT
            p.id,
            p.name,
            p.price,
            p.discount,
            p.status,
            i.url AS thumbnail
        FROM products p
        LEFT JOIN product_images pi 
            ON pi.product_id = p.id AND UPPER(pi.type) = 'THUMBNAIL'
        LEFT JOIN images i 
            ON pi.image_id = i.id
        WHERE p.id = ?
    """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product p = new Product();
                    p.setId(rs.getInt("id"));
                    p.setName(rs.getString("name"));
                    p.setPrice(rs.getDouble("price"));
                    p.setDiscount(rs.getDouble("discount"));
                    p.setStatus(ProductStatus.fromString(rs.getString("status")));
                    p.setThumbnail(rs.getString("thumbnail"));
                    return p;
                }
            }
        }
        return null;
    }

    // ========== New: get product detail by product id ==========
    public ProductDetail getProductDetailByProductId(int productId) throws SQLException {
        String sql = """
        SELECT pd.*, b.name AS brand_name
        FROM product_details pd
        LEFT JOIN brands b ON pd.brand_id = b.id
        WHERE pd.product_id = ?
    """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ProductDetail d = new ProductDetail();
                    d.setProductId(rs.getInt("product_id"));

                    int brandId = rs.getInt("brand_id");
                    String brandName = rs.getString("brand_name");
                    if (!rs.wasNull()) d.setBrand(new Brand(brandId, brandName));

                    d.setOs(rs.getString("os"));
                    d.setRam(rs.getInt("ram"));
                    if (rs.wasNull()) d.setRam(null);
                    d.setStorage(rs.getInt("storage"));
                    if (rs.wasNull()) d.setStorage(null);
                    d.setBatteryCapacity(rs.getInt("battery_capacity"));
                    if (rs.wasNull()) d.setBatteryCapacity(null);
                    d.setScreenSize(rs.getDouble("screen_size"));
                    if (rs.wasNull()) d.setScreenSize(null);
                    d.setScreenResolution(rs.getString("screen_resolution"));
                    d.setMobileNetwork(rs.getString("mobile_network"));
                    d.setCpu(rs.getString("cpu"));
                    d.setGpu(rs.getString("gpu"));
                    d.setWaterResistance(rs.getString("water_resistance"));
                    d.setMaxChargeWatt(rs.getInt("max_charge_watt"));
                    if (rs.wasNull()) d.setMaxChargeWatt(null);
                    d.setDesign(rs.getString("design"));
                    d.setMemoryCard(rs.getString("memory_card"));
                    d.setCpuSpeed(rs.getDouble("cpu_speed"));
                    if (rs.wasNull()) d.setCpuSpeed(null);
                    d.setReleaseDate(rs.getTimestamp("release_date"));
                    d.setRating(rs.getDouble("rating"));
                    if (rs.wasNull()) d.setRating(null);
                    d.setDescription(rs.getString("description"));
                    return d;
                }
            }
        }
        return null;
    }

    public List<String> getProductDetailImages(int productId) throws SQLException {
        List<String> urls = new ArrayList<>();
        String sql = """
        SELECT i.url
        FROM product_images pi
        JOIN images i ON pi.image_id = i.id
        WHERE pi.product_id = ? AND UPPER(pi.type) = 'DETAIL'
        ORDER BY pi.id ASC
    """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    urls.add(rs.getString("url"));
                }
            }
        }
        return urls;
    }

}
