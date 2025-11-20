package com.example.DAO;

import com.example.DTO.Brands.GetBrandResponseDTO;
import com.example.DTO.Products.GetProductsPagingResponseDTO;
import com.example.Service.Database.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BrandDAO {
    public BrandDAO() {
    }

    public List<GetBrandResponseDTO> getBrandsWithProductCount() throws SQLException {
        String sql = """
                    SELECT b.id, b.name, COUNT(pd.product_id) AS product_count
                    FROM brands b
                    LEFT JOIN product_details pd ON b.id = pd.brand_id
                    GROUP BY b.id, b.name
                    ORDER BY product_count DESC;
                """;

        List<GetBrandResponseDTO> results = new ArrayList<>();


        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                results.add(GetBrandResponseDTO.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .productCount(rs.getInt("product_count"))
                        .build());
            }
            return results;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
