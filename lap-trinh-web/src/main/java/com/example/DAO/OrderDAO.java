package com.example.DAO;

import com.example.Model.Order;
import com.example.Model.OrderStatus;
import com.example.Service.Database.JDBCConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    public OrderDAO(Connection conn) {
    }

    public List<Order> findOrdersByUserIdPaging(int userId, int offset, int limit) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = """
                SELECT * 
                FROM orders 
                WHERE user_id = ? 
                ORDER BY created_at 
                    DESC LIMIT ? 
                OFFSET ?
                """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, limit);
            stmt.setInt(3, offset);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Order order = Order.builder()
                            .id(rs.getInt("id"))
                            .userId(rs.getInt("user_id"))
                            .price(rs.getDouble("price"))
                            .status(OrderStatus.fromString(rs.getString("status")))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build();
                    orders.add(order);
                }
            }
            return orders;
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
