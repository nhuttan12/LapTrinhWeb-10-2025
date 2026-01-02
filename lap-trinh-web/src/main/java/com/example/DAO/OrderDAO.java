package com.example.DAO;

import com.example.Model.Cart;
import com.example.Model.CartDetail;
import com.example.Model.Order;
import com.example.Model.ShippingStatus;
import com.example.Service.Database.JDBCConnection;

import java.sql.*;
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
                            .status(ShippingStatus.fromString(rs.getString("status")))
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
    public void updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ?, updated_at = NOW() WHERE id = ?";

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }
    public int createOrderFromCart(Cart cart) throws SQLException {

        Connection conn = JDBCConnection.getConnection();
        conn.setAutoCommit(false);

        try {
            // 1. Insert order
            String orderSql = "INSERT INTO orders(user_id, price, status, created_at, updated_at) VALUES (?, ?, 'pending', NOW(), NOW())";
            double total = 0;

            for (CartDetail d : cart.getCartDetails()) {
                total += d.getProduct().getPrice() * d.getQuantity();
            }

            PreparedStatement ps = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, cart.getUserId());
            ps.setDouble(2, total);
            ps.executeUpdate();

            int orderId = -1;
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) orderId = rs.getInt(1);

            // 2. Insert order_details
            String detailSql =
                    "INSERT INTO order_details(order_id, product_id, quantity, price, created_at) " +
                            " VALUES (?, ?, ?, ?, NOW())";

            PreparedStatement ps2 = conn.prepareStatement(detailSql);

            for (CartDetail d : cart.getCartDetails()) {
                ps2.setInt(1, orderId);
                ps2.setInt(2, d.getProduct().getId());
                ps2.setInt(3, d.getQuantity());
                ps2.setDouble(4, d.getProduct().getPrice());
                ps2.addBatch();
            }
            ps2.executeBatch();

            conn.commit();
            return orderId;

        } catch (Exception e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    public Order findOrderById(int orderId) throws SQLException {
        String sql = "SELECT * FROM orders WHERE id = ?";
        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return Order.builder()
                        .id(rs.getInt("id"))
                        .userId(rs.getInt("user_id"))
                        .price(rs.getDouble("price"))
                        .status(ShippingStatus.fromString(rs.getString("status")))
                        .createdAt(rs.getTimestamp("created_at"))
                        .updatedAt(rs.getTimestamp("updated_at"))
                        .build();
            }
        }
        return null;
    }

    public boolean cancelOrder(int orderId, int userId) throws SQLException {
        String sql = """
        UPDATE orders 
        SET status = 'CANCELLED', updated_at = NOW()
        WHERE id = ? AND user_id = ? AND status = 'PENDING'
    """;

        try (Connection conn = JDBCConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, orderId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;
        }
    }
}

