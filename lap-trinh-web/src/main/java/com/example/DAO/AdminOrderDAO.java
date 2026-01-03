package com.example.DAO;

import com.example.DTO.Common.PagingMetaData;
import com.example.DTO.Common.PagingResponse;
import com.example.Model.Order;
import com.example.Model.PaymentStatus;
import com.example.Model.ShippingStatus;
import com.example.Model.User;
import com.example.Service.Database.JDBCConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminOrderDAO {
    private final Connection conn;

    public AdminOrderDAO(Connection conn) {
        this.conn = conn;
    }

    // Lấy tất cả order với paging
    public PagingResponse<Order> getAllOrders(
            int offset,
            int currentPage,
            int pageSize
    ) throws SQLException {
        List<Order> orders = new ArrayList<>();

        String countSql = """
                SELECT COUNT(*) AS total
                FROM orders
                """;

        String dataSql = """
                SELECT 
                    o.id,
                    o.user_id,
                    u.username,
                    o.price,
                    o.payment_status,
                    o.shipping_status,
                    o.created_at,
                    o.updated_at
                FROM orders o
                JOIN users u ON o.user_id = u.id
                ORDER BY o.created_at DESC
                LIMIT ? OFFSET ?
                """;

        PagingMetaData meta;

        try (Connection conn = JDBCConnection.getConnection()) {

            // ---------- COUNT ----------
            int totalItems = 0;
            try (PreparedStatement countStmt = conn.prepareStatement(countSql);
                 ResultSet rs = countStmt.executeQuery()) {

                if (rs.next()) {
                    totalItems = rs.getInt("total");
                }
            }

            // ---------- MAIN QUERY ----------
            try (PreparedStatement ps = conn.prepareStatement(dataSql)) {
                ps.setInt(1, pageSize);
                ps.setInt(2, offset);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        orders.add(mapRowToOrder(rs));
                    }
                }
            }

            // ---------- META ----------
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            boolean hasNext = currentPage < totalPages;
            boolean hasPrevious = currentPage > 1;

            meta = PagingMetaData.builder()
                    .currentPage(currentPage)
                    .pageSize(pageSize)
                    .totalItems(totalItems)
                    .totalPages(totalPages)
                    .hasNext(hasNext)
                    .hasPrevious(hasPrevious)
                    .build();

            return PagingResponse.<Order>builder()
                    .items(orders)
                    .meta(meta)
                    .build();
        }

    }

    // Lấy order theo id
    public Order getOrderById(int orderId) throws SQLException {
        String sql = "SELECT id, user_id, price, payment_status, shipping_status, created_at, updated_at " +
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
    // Cập nhật trạng thái đơn hàng
    public boolean updateOrderStatus(int orderId, PaymentStatus paymentStatus, ShippingStatus shippingStatus) throws SQLException {
        String sql = "UPDATE orders SET payment_status = ?, shipping_status = ?, updated_at = NOW() WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // PaymentStatus có getStatus()
            ps.setString(1, paymentStatus.getStatus());
            // ShippingStatus có getOrderStatus()
            ps.setString(2, shippingStatus.getStatus());
            ps.setInt(3, orderId);
            return ps.executeUpdate() > 0;
        }
    }


    private Order mapRowToOrder(ResultSet rs) throws SQLException {
        User user = User.builder()
                .id(rs.getInt("user_id"))
                .username(rs.getString("username"))
                .build();

        return Order.builder()
                .id(rs.getInt("id"))
                .userId(rs.getInt("user_id"))
                .price(rs.getDouble("price"))
                .paymentStatus(PaymentStatus.fromString(rs.getString("payment_status")))
                .shippingStatus(ShippingStatus.fromString(rs.getString("shipping_status")))
                .createdAt(rs.getTimestamp("created_at"))
                .updatedAt(rs.getTimestamp("updated_at"))
                .user(user)
                .build();
    }
}
