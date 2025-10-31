package com.example.DAO;

import com.example.Model.Order;
import com.example.Model.OrderStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {
    private final Connection conn;

    public OrderDAO(Connection conn) {
        this.conn = conn;
    }

    public List<Order> findOrdersByUserIdPaging(int userId, int page, int pageSize) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY created_at DESC LIMIT ? OFFSET ?";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                int offset = (page - 1) * pageSize;

                stmt.setInt(1, userId);
                stmt.setInt(2, pageSize);
                stmt.setInt(3, offset);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        Order order = Order.builder()
                                .id(rs.getInt("id"))
                                .userId(rs.getInt("user_id"))
                                .price(rs.getDouble("price"))
                                .status(OrderStatus.valueOf(rs.getString("status")))
                                .createdAt(rs.getTimestamp("created_at"))
                                .updatedAt(rs.getTimestamp("updated_at"))
                                .build();
                        orders.add(order);
                    }
                }
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    System.err.println("Transaction rolled back due to error.");
                } catch (SQLException rollbackEx) {
                    rollbackEx.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return orders;
    }
}
