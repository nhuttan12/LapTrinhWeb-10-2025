package com.example.DAO;


import com.example.Model.ProductStatus;
import com.example.Service.Database.JDBCConnection;
import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.util.*;

public class CSVSeeder {
    public static void main(String[] args) {
        String csvPath = "D:\\LapTrinhWeb-10-2025\\lap-trinh-web\\tgdd_products_full.csv";
        CSVSeeder.importProducts(csvPath);
    }

    private static final List<String> DETAIL_FIELDS = Arrays.asList(
            "bluetooth","bảo mật nâng cao","chip xử lý (cpu)","chip đồ họa (gpu)","chất liệu","công nghệ màn hình",
            "công nghệ pin","cổng kết nối/sạc","danh bạ","dung lượng còn lại (khả dụng) khoảng","dung lượng lưu trữ",
            "dung lượng pin","ghi âm","gps","hãng","hệ điều hành","hỗ trợ sạc tối đa","jack tai nghe",
            "kháng nước, bụi","kích thước, khối lượng","kết nối khác","loại pin","màn hình rộng","mạng di động",
            "mặt kính cảm ứng","nghe nhạc","quay phim camera sau","radio","ram","sim","sạc kèm theo máy","thiết kế",
            "thẻ nhớ","thời điểm ra mắt","tính năng camera sau","tính năng camera trước","tính năng đặc biệt",
            "tốc độ cpu","wifi","xem phim","đèn flash camera sau","độ phân giải camera sau","độ phân giải camera trước",
            "độ phân giải màn hình","độ sáng tối đa"
    );

    public static void importProducts(String csvPath) {
        try (Connection conn = JDBCConnection.getConnection();
             CSVReader reader = new CSVReader(new FileReader(csvPath))) {

            conn.setAutoCommit(false);

            String[] header = reader.readNext(); // dòng đầu là header
            if (header == null) {
                System.out.println("File CSV trống.");
                return;
            }

            String[] row;
            while ((row = reader.readNext()) != null) {
                Map<String, String> data = new LinkedHashMap<>();
                for (int i = 0; i < header.length; i++) {
                    data.put(header[i].trim(), row[i].trim());
                }

                insertProductWithDetails(conn, data);
            }

            conn.commit();
            System.out.println("Import thành công!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void insertProductWithDetails(Connection conn, Map<String, String> data) throws SQLException {
        String name = data.get("name");
        double price = parseDouble(data.get("price"));
        double discount = parseDouble(data.get("discount"));
        String status = data.getOrDefault("status", "active");
        String category = data.getOrDefault("category", "");

        // 1️⃣ Insert product
        String insertProductSQL = """
            INSERT INTO products (name, price, discount, status, category)
            VALUES (?, ?, ?, ?, ?) RETURNING id
        """;

        int productId;
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

        // 2️⃣ Build description
        StringBuilder description = new StringBuilder();
        for (String field : DETAIL_FIELDS) {
            String value = data.get(field);
            if (value != null && !value.isEmpty()) {
                description.append(field).append(": ").append(value).append("; ");
            }
        }

        // 3️⃣ Insert product_details
        String insertDetailSQL = """
            INSERT INTO product_details (product_id, size, color, description, rating)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (PreparedStatement ps = conn.prepareStatement(insertDetailSQL)) {
            ps.setInt(1, productId);
            ps.setString(2, data.getOrDefault("size", ""));
            ps.setString(3, data.getOrDefault("color", ""));
            ps.setString(4, description.toString());
            ps.setDouble(5, parseDouble(data.getOrDefault("rating", "0")));
            ps.executeUpdate();
        }

        // 4️⃣ Insert image_main (thumbnail)
        String imageMainUrl = data.get("image_main");
        if (imageMainUrl != null && !imageMainUrl.isEmpty()) {
            int imageId = insertImage(conn, imageMainUrl, "active");
            insertProductImage(conn, imageId, productId, "thumbnail");
        }

        // 5️⃣ Insert images_detail (gallery)
        String imagesDetail = data.get("images_detail");
        if (imagesDetail != null && !imagesDetail.isEmpty()) {
            String[] urls = imagesDetail.split("\\|");
            for (String url : urls) {
                int imageId = insertImage(conn, url, "active");
                insertProductImage(conn, imageId, productId, "gallery");
            }
        }

        System.out.println("Đã insert product: " + name + " (ID: " + productId + ")");
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
            return Double.parseDouble(val.replace(",", "."));
        } catch (Exception e) {
            return 0.0;
        }
    }

}
