package com.example.DAO;


import com.example.Model.BrandStatus;
import com.example.Service.Database.JDBCConnection;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.sql.*;
import java.util.*;

public class CSVSeeder {
    public static void main(String[] args) {
        String csvPath = "D:\\LapTrinhWeb-10-2025\\lap-trinh-web\\tgdd_products_full.csv";
        CSVSeeder.importProducts(csvPath);
    }

    /**
     * Fields that will be appended into "description" instead of separate DB columns.
     */
    private static final List<String> DESCRIPTION_FIELDS = Arrays.asList(
            "bảo mật nâng cao", "chất liệu", "công nghệ màn hình", "công nghệ pin",
            "cổng kết nối/sạc", "danh bạ", "ghi âm", "gps", "jack tai nghe",
            "kích thước, khối lượng", "kết nối khác", "loại pin", "mặt kính cảm ứng",
            "nghe nhạc", "quay phim camera sau", "radio", "sim", "sạc kèm theo máy",
            "tính năng camera sau", "tính năng camera trước", "tính năng đặc biệt",
            "wifi", "xem phim", "đèn flash camera sau", "độ phân giải camera sau",
            "độ phân giải camera trước", "độ sáng tối đa"
    );

    private static final Map<String, Integer> BRAND_CACHE = new HashMap<>();

    public static void importProducts(String csvPath) {
        try (Connection conn = JDBCConnection.getConnection();
             CSVReader reader = new CSVReader(new FileReader(csvPath))) {

            conn.setAutoCommit(false);

            // ✅ Ensure images sequence continues after default image (id=1)
            try (PreparedStatement ps = conn.prepareStatement("ALTER SEQUENCE images_id_seq RESTART WITH 2")) {
                ps.executeUpdate();
                System.out.println("Sequence 'images_id_seq' restarted from 2 ✅");
            }

            String[] header = reader.readNext();
            if (header == null) {
                System.out.println("File CSV trống.");
                return;
            }

            String[] row;
            while ((row = reader.readNext()) != null) {
                Map<String, String> data = new LinkedHashMap<>();
                for (int i = 0; i < header.length; i++) {
                    data.put(header[i].trim().toLowerCase(), row[i].trim());
                }

                insertProductWithDetails(conn, data);
            }

            conn.commit();
            System.out.println("✅ Import thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertProductWithDetails(Connection conn, Map<String, String> data) throws SQLException {
        // --- Product ---
        String name = data.get("name");
        double price = parseDouble(data.get("price"));
        double discount = parseDouble(data.get("discount"));
        String status = data.getOrDefault("status", "active");
        String category = data.getOrDefault("category", "");

        int productId;
        String insertProductSQL = """
                    INSERT INTO products (name, price, discount, status, category)
                    VALUES (?, ?, ?, ?, ?) RETURNING id
                """;

        try (PreparedStatement ps = conn.prepareStatement(insertProductSQL)) {
            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setDouble(3, discount);
            ps.setString(4, status);
            ps.setString(5, category);
            ResultSet rs = ps.executeQuery();
            rs.next();
            productId = rs.getInt("id");
        }

        // --- Handle brand ---
        String brandName = data.getOrDefault("brand", "").trim();
        Integer brandId = null;
        if (!brandName.isEmpty()) {
            brandId = getOrInsertBrand(conn, brandName);
        }

        // --- Build description ---
        StringBuilder description = new StringBuilder();
        for (String field : DESCRIPTION_FIELDS) {
            String value = data.get(field);
            if (value != null && !value.isEmpty()) {
                description.append(field).append(": ").append(value).append("; ");
            }
        }

        // --- Insert product_details ---
        String insertDetailSQL = """
                    INSERT INTO product_details (
                        product_id, brand_id, os, ram, storage, battery_capacity,
                        screen_size, screen_resolution, mobile_network, cpu, gpu,
                        water_resistance, max_charge_watt, design, memory_card, cpu_speed,
                        release_date, description
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(insertDetailSQL)) {
            ps.setInt(1, productId);
            if (brandId != null) ps.setInt(2, brandId);
            else ps.setNull(2, Types.INTEGER);
            ps.setString(3, data.getOrDefault("hệ điều hành", ""));
            ps.setString(4, data.getOrDefault("ram", ""));
            ps.setString(5, data.getOrDefault("dung lượng lưu trữ", ""));
            ps.setInt(6, parseInt(data.get("dung lượng pin")));
            ps.setDouble(7, parseDouble(data.get("màn hình rộng")));
            ps.setString(8, data.getOrDefault("độ phân giải màn hình", ""));
            ps.setString(9, data.getOrDefault("mạng di động", ""));
            ps.setString(10, data.getOrDefault("chip xử lý (cpu)", ""));
            ps.setString(11, data.getOrDefault("chip đồ họa (gpu)", ""));
            ps.setString(12, data.getOrDefault("kháng nước, bụi", ""));
            ps.setInt(13, parseInt(data.get("hỗ trợ sạc tối đa")));
            ps.setString(14, data.getOrDefault("thiết kế", ""));
            ps.setString(15, data.getOrDefault("thẻ nhớ", ""));
            ps.setDouble(16, parseDouble(data.get("tốc độ cpu")));
            ps.setTimestamp(17, parseTimestamp(data.get("thời điểm ra mắt")));
            ps.setString(18, description.toString());
            ps.executeUpdate();
        }

        // --- Insert image_main (thumbnail) ---
        String imageMainUrl = data.get("image_main");
        if (imageMainUrl != null && !imageMainUrl.isEmpty()) {
            int imageId = insertImage(conn, imageMainUrl, "active");
            insertProductImage(conn, imageId, productId, "thumbnail");
        }

        // --- Insert images_detail (gallery) ---
        String imagesDetail = data.get("images_detail");
        if (imagesDetail != null && !imagesDetail.isEmpty()) {
            String[] urls = imagesDetail.split("\\|");
            for (String url : urls) {
                int imageId = insertImage(conn, url, "active");
                insertProductImage(conn, imageId, productId, "gallery");
            }
        }

        System.out.println("✅ Inserted: " + name + " (ID: " + productId + ") - Brand: " + brandName);
    }

    /**
     * Get brand_id from cache or DB, or insert if not exists.
     */
    private static int getOrInsertBrand(Connection conn, String brandName) throws SQLException {
        // Check cache
        if (BRAND_CACHE.containsKey(brandName)) {
            return BRAND_CACHE.get(brandName);
        }

        // Try to find in DB
        String selectSQL = "SELECT id FROM brands WHERE LOWER(name) = LOWER(?)";
        try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
            ps.setString(1, brandName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                BRAND_CACHE.put(brandName, id);
                return id;
            }
        }

        // Insert new brand
        String insertSQL = "INSERT INTO brands (name, status) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
            ps.setString(1, brandName);
            ps.setString(2, BrandStatus.ACTIVE.getBrandStatus());
            ResultSet rs = ps.executeQuery();
            rs.next();
            int id = rs.getInt("id");
            BRAND_CACHE.put(brandName, id);
            System.out.println("🆕 New brand added: " + brandName + " (ID: " + id + ")");
            return id;
        }
    }

    private static int insertImage(Connection conn, String url, String status) throws SQLException {
        String sql = "INSERT INTO images (url, status) VALUES (?, ?) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, url);
            ps.setString(2, status);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("id");
        }
    }

    private static void insertProductImage(Connection conn, int imageId, int productId, String type) throws SQLException {
        String sql = "INSERT INTO product_images (image_id, product_id, type) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, imageId);
            ps.setInt(2, productId);
            ps.setString(3, type);
            ps.executeUpdate();
        }
    }

    private static double parseDouble(String val) {
        try {
            if (val == null || val.isEmpty()) return 0.0;
            return Double.parseDouble(val.replace(",", ".").replaceAll("[^0-9.]", ""));
        } catch (Exception e) {
            return 0.0;
        }
    }

    private static int parseInt(String val) {
        try {
            if (val == null || val.isEmpty()) return 0;
            return Integer.parseInt(val.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }

    private static Timestamp parseTimestamp(String val) {
        try {
            if (val == null || val.isEmpty()) return null;

            // Case 1: Full date
            if (val.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return Timestamp.valueOf(val + " 00:00:00");
            }

            // Case 2: Month/year format like 05/2023
            if (val.matches("\\d{2}/\\d{4}")) {
                String[] parts = val.split("/");
                return Timestamp.valueOf(parts[1] + "-" + parts[0] + "-01 00:00:00");
            }

            // Case 3: Year only
            if (val.matches("\\d{4}")) {
                return Timestamp.valueOf(val + "-01-01 00:00:00");
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

}
