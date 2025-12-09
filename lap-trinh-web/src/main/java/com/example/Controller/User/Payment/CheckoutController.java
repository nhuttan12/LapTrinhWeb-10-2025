package com.example.Controller.User.Payment;

import com.example.Model.Cart;
import com.example.Model.User;
import com.example.Model.UserDetail;
import com.example.Service.Cart.CartService;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.User.UserDetailService;
import com.example.Service.User.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/checkout")
public class CheckoutController extends HttpServlet {

    private CartService cartService;
    private UserService userService;
    private UserDetailService userDetailService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = JDBCConnection.getConnection();
            cartService = new CartService(conn);
            userService = new UserService(conn);
            userDetailService = new UserDetailService(conn);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize services in CheckoutController", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // Load cart
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null || cart.getCartDetails() == null || cart.getCartDetails().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/cart");
            return;
        }
        req.setAttribute("cart", cart);

        // Load user info
        User user = userService.getUserById(userId);
        UserDetail detail = userDetailService.getByUserId(userId);

        req.setAttribute("fullName", user.getFullName());
        req.setAttribute("email", user.getEmail());
        req.setAttribute("phone", detail != null ? detail.getPhone() : "");
        req.setAttribute("address", detail != null ? detail.getAddress1() : "");

        req.getRequestDispatcher("/user/checkout.jsp").forward(req, resp);
    }
}
