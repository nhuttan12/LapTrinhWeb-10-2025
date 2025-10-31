package com.example.Controller.User.Profile;

import com.example.DTO.Order.OrderDetailUserResponseDTO;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.Order.OrderDetailService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/order-detail")
public class OrderDetailController extends HttpServlet {
    private OrderDetailService orderDetailService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = JDBCConnection.getConnection();
            orderDetailService = new OrderDetailService(conn);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = (Integer) session.getAttribute("userId");

        int orderId = Integer.parseInt(req.getParameter("orderId"));

        List<OrderDetailUserResponseDTO> orderDetails = orderDetailService.getOrderDetailByOrderId(orderId, userId, 1, 10);

        req.setAttribute("orderDetails", orderDetails);
        req.getRequestDispatcher("/user/order-detail.jsp").forward(req, resp);
    }
}
