package com.example.DAO;

import com.example.Model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {
    private final Connection conn;

    public CartDAO(Connection conn) {
        this.conn = conn;
    }

    /** Lấy giỏ hàng OPEN theo user */
    public Cart getActiveCartByUserId(int userId) {
        String sql = "SELECT * FROM carts WHERE user_id = ? AND status = ? LIMIT 1";
        Cart cart = null;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, CartStatus.OPEN.getStatus());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cart = Cart.builder()
                            .id(rs.getInt("id"))
                            .userId(rs.getInt("user_id"))
                            .status(CartStatus.fromString(rs.getString("status")))
                            .createdAt(rs.getTimestamp("created_at"))
                            .updatedAt(rs.getTimestamp("updated_at"))
                            .build();

                    // lấy danh sách cart details và gán vào cart
                    List<CartDetail> details = getCartDetailsByCartId(cart.getId());
                    cart.setCartDetails(details);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }

    /** Tạo giỏ hàng mới */
    public int createCart(int userId) {
        String sql = "INSERT INTO carts (user_id, status, created_at, updated_at) VALUES (?, ?, NOW(), NOW())";
        int id = -1;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, CartStatus.OPEN.getStatus());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) id = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    /** Lấy danh sách cart detail theo cart id */
    public List<CartDetail> getCartDetailsByCartId(int cartId) {
        List<CartDetail> list = new ArrayList<>();

        String sql = "SELECT DISTINCT cd.*, p.id as pid, p.name, p.price, img.url as thumbnail " +
                "FROM cart_details cd " +
                "JOIN products p ON cd.product_id = p.id " +
                "LEFT JOIN product_images pi ON pi.product_id = p.id AND pi.type = 'thumbnail' " +
                "LEFT JOIN images img ON img.id = pi.image_id " +
                "WHERE cd.cart_id = ? AND cd.status = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.setString(2, CartDetailStatus.ACTIVE.getStatus());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartDetail cd = new CartDetail();
                    cd.setId(rs.getInt("id"));
                    cd.setCartId(rs.getInt("cart_id"));
                    cd.setProductId(rs.getInt("product_id"));
                    cd.setQuantity(rs.getInt("quantity"));
                    cd.setStatus(CartDetailStatus.fromString(rs.getString("status")));
                    cd.setCreatedAt(rs.getTimestamp("created_at"));
                    cd.setUpdatedAt(rs.getTimestamp("updated_at"));

                    // set product
                    Product p = new Product();
                    p.setId(rs.getInt("pid"));
                    p.setName(rs.getString("name"));
                    p.setPrice(rs.getDouble("price"));
                    p.setThumbnail(rs.getString("thumbnail"));  // Sử dụng alias mới 'thumbnail'
                    cd.setProduct(p);

                    list.add(cd);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    /** Thêm hoặc update sản phẩm trong giỏ */
    public void addProductToCartDetail(int cartId, int productId, int quantity) {
        String checkSql = "SELECT quantity FROM cart_details WHERE cart_id = ? AND product_id = ? AND status = ?";
        String insertSql = "INSERT INTO cart_details (cart_id, product_id, quantity, status, created_at, updated_at) VALUES (?, ?, ?, ?, NOW(), NOW())";
        String updateSql = "UPDATE cart_details SET quantity = quantity + ?, updated_at = NOW() WHERE cart_id = ? AND product_id = ?";

        try (PreparedStatement check = conn.prepareStatement(checkSql)) {
            check.setInt(1, cartId);
            check.setInt(2, productId);
            check.setString(3, CartDetailStatus.ACTIVE.getStatus());

            try (ResultSet rs = check.executeQuery()) {
                if (rs.next()) {
                    try (PreparedStatement update = conn.prepareStatement(updateSql)) {
                        update.setInt(1, quantity);
                        update.setInt(2, cartId);
                        update.setInt(3, productId);
                        update.executeUpdate();
                    }
                } else {
                    try (PreparedStatement insert = conn.prepareStatement(insertSql)) {
                        insert.setInt(1, cartId);
                        insert.setInt(2, productId);
                        insert.setInt(3, quantity);
                        insert.setString(4, CartDetailStatus.ACTIVE.getStatus());
                        insert.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Soft remove sản phẩm khỏi giỏ */
    public void removeProductFromCart(int cartId, int productId) {
        String sql = "UPDATE cart_details " +
                "SET status = ?, updated_at = NOW() " +
                "WHERE cart_id = ? AND product_id = ? AND status = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, CartDetailStatus.REMOVED.getStatus()); // removed
            ps.setInt(2, cartId);
            ps.setInt(3, productId);
            ps.setString(4, CartDetailStatus.ACTIVE.getStatus()); // active (điều kiện bị thiếu)

            int rows = ps.executeUpdate();
            System.out.println("Rows updated = " + rows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Update số lượng sản phẩm trong giỏ */
    public void updateProductQuantity(int cartId, int productId, int quantity) {
        String sql = "UPDATE cart_details SET quantity = ?, updated_at = NOW() WHERE cart_id = ? AND product_id = ? AND status = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartId);
            ps.setInt(3, productId);
            ps.setString(4, CartDetailStatus.ACTIVE.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /** Tạo giỏ hàng cho user nếu chưa có */
    public Cart createCartForUser(int userId) throws SQLException {
        int newId = createCart(userId);
        if (newId == -1) return null;
        return getActiveCartByUserId(userId);
    }
    public void removePurchasedItems(int cartId, List<CartDetail> purchasedItems) throws SQLException {
        String sql = "UPDATE cart_details SET status = ?, updated_at = NOW() " +
                "WHERE cart_id = ? AND product_id = ? AND status = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (CartDetail item : purchasedItems) {
                ps.setString(1, CartDetailStatus.REMOVED.getStatus()); // "removed"
                ps.setInt(2, cartId);
                ps.setInt(3, item.getProductId());
                ps.setString(4, CartDetailStatus.ACTIVE.getStatus());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

}
