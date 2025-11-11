package com.example.DAO;

import com.example.Model.Cart;
import com.example.Model.CartStatus;

import java.sql.*;

public class CartDAO {
    private final Connection conn;

    public CartDAO(Connection conn) {
        this.conn = conn;
    }
    /**
     * Lấy giỏ hàng đang mở (OPEN) của user
     */
    public Cart getActiveCartByUserId(int userId) {
        String sql = "SELECT * FROM cart WHERE user_id = ? AND status = 'OPEN' LIMIT 1";
        Cart cart = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cart = Cart.builder()
                            .id(rs.getInt("id"))
                            .userId(rs.getInt("user_id"))
                            .status(CartStatus.valueOf(rs.getString("status")))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cart;
    }

    /**
     * Tạo mới 1 giỏ hàng (OPEN)
     */
    public int createCart(int userId) {
        String sql = "INSERT INTO cart (user_id, status, created_at, updated_at) VALUES (?, 'OPEN', NOW(), NOW())";
        int cartId = -1;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    cartId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartId;
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     * Nếu sản phẩm đã tồn tại -> tăng số lượng
     * Nếu chưa có -> thêm mới
     */
    public void addProductToCartDetail(int cartId, int productId, int quantity) {
        String checkSql = "SELECT quantity FROM cart_detail WHERE cart_id = ? AND product_id = ? AND status = 'ACTIVE'";
        String insertSql = "INSERT INTO cart_detail (cart_id, product_id, quantity, status, created_at, updated_at) VALUES (?, ?, ?, 'ACTIVE', NOW(), NOW())";
        String updateSql = "UPDATE cart_detail SET quantity = quantity + ?, updated_at = NOW() WHERE cart_id = ? AND product_id = ?";

        try (PreparedStatement check = conn.prepareStatement(checkSql)) {
            check.setInt(1, cartId);
            check.setInt(2, productId);
            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    // Nếu sản phẩm đã tồn tại -> update số lượng
                    try (PreparedStatement update = conn.prepareStatement(updateSql)) {
                        update.setInt(1, quantity);
                        update.setInt(2, cartId);
                        update.setInt(3, productId);
                        update.executeUpdate();
                    }
                } else {
                    // Nếu chưa có -> insert mới
                    try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                        insert.setInt(1, cartId);
                        insert.setInt(2, productId);
                        insert.setInt(3, quantity);
                        insert.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa (hoặc vô hiệu hóa) sản phẩm trong giỏ hàng
     */
    public void removeProductFromCart(int cartId, int productId) {
        String sql = "UPDATE cart_detail SET status = 'INACTIVE', updated_at = NOW() WHERE cart_id = ? AND product_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
