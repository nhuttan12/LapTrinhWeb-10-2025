package com.example.Controller.User.Profile;

import com.example.Model.ShippingStatus;
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

        HttpSession session = req.getSession(); // tạo session nếu chưa có
        int userId = (Integer) session.getAttribute("userId");
        int orderId = Integer.parseInt(req.getParameter("orderId"));

        try {
            boolean success = orderService.cancelOrder(orderId, userId);

            if (success) {
                session.setAttribute("successMessage", "Đơn hàng đã hủy thành công!");
            } else {
                ShippingStatus currentStatus = orderService.getShippingStatus(orderId);
                String message = switch (currentStatus) {
                    case SHIPPED, COMPLETED -> "Đơn hàng đã giao hoặc hoàn tất, không thể hủy.";
                    case CANCELLED -> "Đơn hàng đã hủy trước đó.";
                    default -> "Không thể hủy đơn hàng này.";
                };
                session.setAttribute("errorMessage", message);
            }

            resp.sendRedirect(req.getContextPath() + "/order-detail?orderId=" + orderId);

        } catch (RuntimeException e) { // chỉ catch RuntimeException thay vì SQLException
            throw new ServletException("Lỗi khi hủy đơn hàng", e);
        }
    }
}
