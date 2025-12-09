package com.example.Controller.User.Profile;

import com.example.Service.Database.JDBCConnection;
import com.example.Service.Order.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/cancel-order")
public class CancelOrderController extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        try {
            orderService = new OrderService(JDBCConnection.getConnection());
        } catch (SQLException e) {
            throw new ServletException("Cannot connect DB", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        int userId = (Integer) session.getAttribute("userId");
        int orderId = Integer.parseInt(req.getParameter("orderId"));

        boolean success = orderService.cancelOrder(orderId, userId);

        if (success) {
            resp.sendRedirect(req.getContextPath() + "/order-detail?orderId=" + orderId);
        } else {
            req.setAttribute("error", "Không thể hủy đơn hàng này.");
            req.getRequestDispatcher("/user/order-detail.jsp").forward(req, resp);
        }
    }
}
