package com.example.Controller.User.Profile;

import com.example.Model.Cart;
import com.example.Service.Cart.CartService;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.Order.OrderServiceSimple;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@WebServlet("/create-order")
public class CreateOrderController extends HttpServlet {

    private OrderServiceSimple orderService;
    private CartService cartService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = JDBCConnection.getConnection();
            cartService = new CartService(conn);
            orderService = new OrderServiceSimple(conn);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        Integer userId = (Integer) req.getSession().getAttribute("userId");
        if (userId == null) {
            resp.sendRedirect("login");
            return;
        }

        Cart cart = cartService.getCartByUserId(userId);

        if (cart == null || cart.getCartDetails().isEmpty()) {
            resp.sendRedirect("cart");
            return;
        }

        int orderId = orderService.createOrderFromCart(cart); // <-- BẠN SẼ ĐƯỢC TÔI VIẾT SERVICE NÀY

        // redirect sang thanh toán
        resp.sendRedirect("payment-page?orderId=" + orderId);
    }
}

