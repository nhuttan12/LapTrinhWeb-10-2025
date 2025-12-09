package com.example.DAO;

import com.example.Model.Order;
import com.example.Model.OrderStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminOrderDAO {
    private final Connection conn;

    public AdminOrderDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy tất cả order với paging
    public List<Order> getAllOrders(int page, int pageSize) throws SQLException {
        List<Order> orders = new ArrayList<>();
        int offset = (page - 1) * pageSize;

        String sql = "SELECT id, user_id, price, status, created_at, updated_at " +
                "FROM orders ORDER BY created_at DESC LIMIT ? OFFSET ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                orders.add(mapRowToOrder(rs));
            }
        }
        return orders;
    }

    // Lấy order theo id
    public Order getOrderById(int orderId) throws SQLException {
        String sql = "SELECT id, user_id, price, status, created_at, updated_at " +
                "FROM orders WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRowToOrder(rs);
            }
        }
        return null;
    }

    // Cập nhật trạng thái đơn hàng
    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ?, updated_at = NOW() WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        return Order.builder()
                .id(rs.getInt("id"))
                .userId(rs.getInt("user_id"))
                .price(rs.getDouble("price"))
                .status(OrderStatus.fromString(rs.getString("status")))
                .createdAt(rs.getTimestamp("created_at"))
                .updatedAt(rs.getTimestamp("updated_at"))
                .build();
    }
}
