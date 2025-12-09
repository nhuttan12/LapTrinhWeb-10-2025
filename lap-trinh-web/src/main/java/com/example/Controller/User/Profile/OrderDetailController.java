package com.example.Controller.User.Profile;

import com.example.DTO.Orders.OrderDetailUserResponseDTO;
import com.example.Model.Order;
import com.example.Model.Payment;
import com.example.Model.User;
import com.example.Model.UserDetail;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.Order.OrderDetailService;
import com.example.Service.Order.OrderService;
import com.example.Service.Payment.PaymentService;
import com.example.Service.User.UserDetailService;
import com.example.Service.User.UserService;

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
@WebServlet("/order-detail")
public class OrderDetailController extends HttpServlet {

    private OrderDetailService orderDetailService;
    private OrderService orderService;
    private PaymentService paymentService;
    private UserService userService;
    private UserDetailService userDetailService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = JDBCConnection.getConnection();

            orderDetailService = new OrderDetailService(conn);
            orderService = new OrderService(conn);
            paymentService = new PaymentService(conn);
            userService = new UserService(conn);
            userDetailService = new UserDetailService(conn);

        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Check login
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            resp.sendRedirect("/login");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        int orderId = Integer.parseInt(req.getParameter("orderId"));

        // Get order
        Order order = orderService.findOrderById(orderId);
        if (order == null || order.getUserId() != userId) {
            resp.sendError(403, "You are not allowed to view this order");
            return;
        }

        req.setAttribute("order", order);
        req.setAttribute("totalPrice", order.getPrice());

        // Get payment
        Payment payment = paymentService.getPaymentByOrderId(orderId);
        req.setAttribute("payment", payment);

        // Get user
        User user = userService.getUserById(userId);
        req.setAttribute("user", user);

        // Get user detail
        UserDetail userDetail = userDetailService.getByUserId(userId);
        req.setAttribute("userDetail", userDetail);

        // Get order details (NO PAGING)
        List<OrderDetailUserResponseDTO> orderDetails =
                orderDetailService.getOrderDetailByOrderId(orderId, userId);

        req.setAttribute("orderDetails", orderDetails);

        req.getRequestDispatcher("/user/order-detail.jsp").forward(req, resp);
    }
}

