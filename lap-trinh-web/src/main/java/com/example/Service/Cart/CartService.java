package com.example.Service.Cart;

import com.example.DAO.CartDAO;
import com.example.Model.Cart;
import com.example.Model.CartDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
     * Lấy danh sách cartDetail
     */
    public List<CartDetail> getCartDetails(int cartId) {
        return cartDAO.getCartDetailsByCartId(cartId);
    }

    /**
     * Thêm sản phẩm vào giỏ
     */
    public void addProductToCart(int userId, int productId, int quantity) {
        Cart cart = cartDAO.getActiveCartByUserId(userId);
        if (cart == null) {
            int cartId = cartDAO.createCart(userId);
            if (cartId < 0) {
                throw new RuntimeException("Failed to create cart for user " + userId);
            }
            cart = cartDAO.getActiveCartByUserId(userId); // lấy lại cart mới tạo
        }

        cartDAO.addProductToCartDetail(cart.getId(), productId, quantity);
    }

    /**
     * Xóa sản phẩm khỏi giỏ
     */
//    public void removeProductFromCart(int userId, int productId) throws SQLException {
//        Cart cart = cartDAO.getActiveCartByUserId(userId);
//        if (cart != null) {
//            cartDAO.removeProductFromCart(cart.getId(), productId);
//        }
//    }
    public void removeProductFromCart(int cartId, int productId) throws SQLException {
        cartDAO.removeProductFromCart(cartId, productId);
    }


    /**
     * Cập nhật số lượng
     */
    public void updateQuantity(int userId, int productId, int quantity) throws SQLException {
        Cart cart = cartDAO.getActiveCartByUserId(userId);
        if (cart != null) {
            cartDAO.updateProductQuantity(cart.getId(), productId, quantity);
        }
    }

    /**
     * Tạo cart cho user
     */
    public Cart createCartForUser(int userId) throws SQLException {
        Cart cart = cartDAO.createCartForUser(userId);
        if (cart == null) throw new RuntimeException("Failed to create cart for user " + userId);
        return cart;
    }

    public void removePurchasedItems(int cartId, List<CartDetail> items) throws SQLException {
        cartDAO.removePurchasedItems(cartId, items);
    }
}
