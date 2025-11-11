package com.example.Service.Cart;

import com.example.DAO.CartDAO;
import com.example.Model.Cart;

import java.sql.Connection;
import java.sql.SQLException;

public class CartService {
    private final CartDAO cartDAO;
    public CartService(Connection conn) {
        this.cartDAO = new CartDAO(conn);
    }

    /**
     * Lấy giỏ hàng hiện tại của user
     */
    public Cart getCartByUserId(int userId) {
        return cartDAO.getActiveCartByUserId(userId);
    }

    /**
     * Thêm sản phẩm vào giỏ hàng (tự tạo giỏ nếu chưa có)
     */
    public void addProductToCart(int userId, int productId, int quantity) {
        try {
            Cart cart = cartDAO.getActiveCartByUserId(userId);
            if (cart == null) {
                // Nếu user chưa có giỏ hàng -> tạo mới
                int newCartId = cartDAO.createCart(userId);
                cartDAO.addProductToCartDetail(newCartId, productId, quantity);
            } else {
                // Nếu đã có giỏ hàng -> thêm sản phẩm
                cartDAO.addProductToCartDetail(cart.getId(), productId, quantity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Xóa sản phẩm khỏi giỏ hàng
     */
    public void removeProductFromCart(int userId, int productId) {
        try {
            Cart cart = cartDAO.getActiveCartByUserId(userId);
            if (cart != null) {
                cartDAO.removeProductFromCart(cart.getId(), productId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
