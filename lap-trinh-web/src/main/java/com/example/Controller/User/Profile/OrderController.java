package com.example.Controller.User.Profile;

import com.example.DTO.Orders.OrderUserResponseDTO;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.Order.OrderService;

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
import java.util.Optional;

@WebServlet("/orders")
public class OrderController extends HttpServlet {
    private OrderService orderService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = JDBCConnection.getConnection();
            orderService = new OrderService(conn);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        int userId = (Integer) session.getAttribute("userId");

        int page = Optional.ofNullable(req.getParameter("page"))
                .map(Integer::parseInt)
                .orElse(1);

        int pageSize = Optional.ofNullable(req.getParameter("pageSize"))
                .map(Integer::parseInt)
                .orElse(10);

        List<OrderUserResponseDTO> orders = orderService.findOrdersByUserId(userId, page, pageSize);
        System.out.println("Get user order: "+orders);

        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/user/orders.jsp").forward(req, resp);
    }
}
