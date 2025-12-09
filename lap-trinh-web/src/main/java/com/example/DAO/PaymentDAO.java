package com.example.DAO;

import com.example.Model.Payment;
import com.example.Model.PaymentMethod;
import com.example.Model.PaymentStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    private final Connection conn;

    public PaymentDAO(Connection conn) {
        this.conn = conn;
    }

    /** Create payment record */
    public int createPayment(Payment payment) throws SQLException {
        String sql = "INSERT INTO payments(order_id, payment_method, amount, status, transaction_id, provider, raw_response, paid_at, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?::jsonb, ?, NOW(), NOW())";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, payment.getOrderId());
            ps.setString(2, payment.getMethod().getMethod());
            ps.setDouble(3, payment.getAmount());
            ps.setString(4, payment.getStatus().getStatus());
            ps.setString(5, payment.getTransactionId());
            ps.setString(6, payment.getProvider());

            // FIX 1: raw_response jsonb
            if (payment.getRawResponse() == null) {
                ps.setNull(7, Types.OTHER);
            } else {
                ps.setObject(7, payment.getRawResponse(), Types.OTHER);
            }

            // FIX 2: paid_at nullable
            if (payment.getPaidAt() == null) {
                ps.setNull(8, Types.TIMESTAMP);
            } else {
                ps.setTimestamp(8, payment.getPaidAt());
            }

            ps.executeUpdate();

            // Get generated id
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
                else return -1;
            }
        }
    }

    public Payment getPaymentById(int id) throws SQLException {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Payment.builder()
                            .id(rs.getInt("id"))
                            .orderId(rs.getInt("order_id"))
                            .amount(rs.getDouble("amount"))
                            .method(PaymentMethod.fromString(rs.getString("payment_method")))
                            .status(PaymentStatus.fromString(rs.getString("status")))
                            .transactionId(rs.getString("transaction_id"))
                            .provider(rs.getString("provider"))
                            .rawResponse(rs.getString("raw_response"))
                            .paidAt(rs.getTimestamp("paid_at"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build();
                }
            }
        }
        return null;
    }
    public int getUserIdByPaymentId(int paymentId) throws SQLException {
        String sql = "SELECT o.user_id FROM payments p " +
                "JOIN orders o ON p.order_id = o.id " +
                "WHERE p.id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("user_id");
                } else {
                    throw new SQLException("Không tìm thấy user cho paymentId: " + paymentId);
                }
            }
        }
    }


    /** Get payments by orderId */
    public List<Payment> getPaymentsByOrderId(int orderId) throws SQLException {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payments WHERE order_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Payment p = Payment.builder()
                            .id(rs.getInt("id"))
                            .orderId(rs.getInt("order_id"))
                            .amount(rs.getDouble("amount"))
                            .method(PaymentMethod.fromString(rs.getString("payment_method")))
                            .status(PaymentStatus.fromString(rs.getString("status")))
                            .transactionId(rs.getString("transaction_id"))
                            .provider(rs.getString("provider"))
                            .rawResponse(rs.getString("raw_response"))
                            .paidAt(rs.getTimestamp("paid_at"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build();

                    list.add(p);
                }
            }
        }
        return list;
    }

    /** Update status */
    public void updatePaymentStatus(int paymentId, PaymentStatus status) throws SQLException {
        String sql = "UPDATE payments SET status = ?, updated_at = NOW() WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.getStatus());
            ps.setInt(2, paymentId);
            ps.executeUpdate();
        }
    }

    public void updateTransactionId(int paymentId, String transactionId) throws SQLException {
        String sql = "UPDATE payments SET transaction_id = ?, updated_at = NOW() WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transactionId);
            ps.setInt(2, paymentId);
            ps.executeUpdate();
        }
    }
    public Payment getPaymentByTransactionId(String transactionId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE transaction_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, transactionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Payment.builder()
                            .id(rs.getInt("id"))
                            .orderId(rs.getInt("order_id"))
                            .amount(rs.getDouble("amount"))
                            .method(PaymentMethod.fromString(rs.getString("payment_method")))
                            .status(PaymentStatus.fromString(rs.getString("status")))
                            .transactionId(rs.getString("transaction_id"))
                            .provider(rs.getString("provider"))
                            .rawResponse(rs.getString("raw_response"))
                            .paidAt(rs.getTimestamp("paid_at"))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build();
                }
            }
        }
        return null;
    }

    public Payment getPaymentByOrderId(int orderId) throws SQLException {
        String sql = "SELECT * FROM payments WHERE order_id = ? ORDER BY created_at DESC LIMIT 1";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return Payment.builder()
                        .id(rs.getInt("id"))
                        .orderId(rs.getInt("order_id"))
                        .amount(rs.getDouble("amount"))
                        .method(PaymentMethod.fromString(rs.getString("payment_method")))
                        .status(PaymentStatus.fromString(rs.getString("status")))
                        .transactionId(rs.getString("transaction_id"))
                        .provider(rs.getString("provider"))
                        .rawResponse(rs.getString("raw_response"))
                        .paidAt(rs.getTimestamp("paid_at"))
                        .createdAt(rs.getTimestamp("created_at"))
                        .updatedAt(rs.getTimestamp("updated_at"))
                        .build();
            }
            return null;
        }
    }
    
}
