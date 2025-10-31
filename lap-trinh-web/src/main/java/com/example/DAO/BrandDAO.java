package com.example.DAO;

import com.example.DTO.Brands.GetBrandResponseDTO;
import com.example.DTO.Products.GetProductsPagingResponseDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BrandDAO {
    private final Connection conn;

    public BrandDAO(Connection conn) {
        this.conn = conn;
    }

    public List<GetBrandResponseDTO> getBrandsWithProductCount() {
        String sql = """
                    SELECT b.id, b.name, COUNT(pd.product_id) AS product_count
                    FROM brands b
                    LEFT JOIN product_details pd ON b.id = pd.brand_id
                    GROUP BY b.id, b.name
                    ORDER BY product_count DESC;
                """;

        List<GetBrandResponseDTO> results = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                results.add(GetBrandResponseDTO.builder()
                        .id(rs.getInt("id"))
                        .name(rs.getString("name"))
                        .productCount(rs.getInt("product_count"))
                        .build());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }
}
