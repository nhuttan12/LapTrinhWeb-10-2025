package com.example.Controller.User.Cart;

import com.example.Model.Cart;
import com.example.Service.Cart.CartService;
import com.example.Service.Database.JDBCConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


@WebServlet("/cart")
public class CartController extends HttpServlet {
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = JDBCConnection.getConnection();
            cartService = new CartService(conn);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize CartService", e);
        }
    }

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
//            throws ServletException, IOException {
//
//        // forward sang file JSP
//        req.getRequestDispatcher("/user/cart.jsp").forward(req, resp);
//    }

    // ===============================

    /**
     * Hiển thị giỏ hàng của user
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Lấy userId từ session hoặc request
            int userId = Integer.parseInt(req.getParameter("userId"));

           Cart cart= cartService.getCartByUserId(userId);
            req.setAttribute("cart", cart);

            // Hiển thị trang giỏ hàng
            req.getRequestDispatcher("/user/cart.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new ServletException("Error loading cart", e);
        }
    }

    /**
     * Xử lý thêm / xóa sản phẩm khỏi giỏ hàng
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try {
            int userId = Integer.parseInt(req.getParameter("userId"));
            int productId = Integer.parseInt(req.getParameter("productId"));

            if ("add".equalsIgnoreCase(action)) {
                int quantity = Integer.parseInt(req.getParameter("quantity"));
                cartService.addProductToCart(userId, productId, quantity);
            } else if ("remove".equalsIgnoreCase(action)) {
                cartService.removeProductFromCart(userId, productId);
            }

            // Sau khi xử lý, redirect về lại giỏ hàng
            resp.sendRedirect(req.getContextPath() + "/cart?userId=" + userId);

        } catch (Exception e) {
            throw new ServletException("Error updating cart", e);
        }
    }
}