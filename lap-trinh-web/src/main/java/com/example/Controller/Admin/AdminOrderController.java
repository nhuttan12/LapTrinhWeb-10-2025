package com.example.Controller.Admin;

import com.example.DAO.AdminOrderDAO;
import com.example.DTO.Common.PagingResponse;
import com.example.DTO.Orders.GetOrdersPagingResponseAdminDTO;
import com.example.Model.Order;
import com.example.Model.PaymentStatus;
import com.example.Model.ShippingStatus;
import com.example.Service.Admin.AdminOrderService;

import com.example.Service.Database.JDBCConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/admin/orders")
public class AdminOrderController extends HttpServlet {
    private AdminOrderService adminOrderService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = JDBCConnection.getConnection();
            adminOrderService = new AdminOrderService(new AdminOrderDAO(conn));
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
    }

    // Hiển thị danh sách tất cả orders
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = Optional.ofNullable(req.getParameter("page")).map(Integer::parseInt).orElse(1);
        int pageSize = Optional.ofNullable(req.getParameter("pageSize")).map(Integer::parseInt).orElse(10);

        try {
            PagingResponse<GetOrdersPagingResponseAdminDTO> orders =
                    adminOrderService.getAllOrderPaging(page, pageSize);
            System.out.println("Logging get order paging admin: "+orders);

            req.setAttribute("orders", orders.getItems());
            req.setAttribute("meta", orders.getMeta());

            req.getRequestDispatcher("/admin/pages/orderManagement/orders.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    // Cập nhật trạng thái order
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderId = Integer.parseInt(req.getParameter("orderId"));
        String status = req.getParameter("status");

        try {
            // Chuyển String sang Enum
            String paymentStatusStr = req.getParameter("paymentStatus");
            String shippingStatusStr = req.getParameter("shippingStatus");

            PaymentStatus paymentStatus = PaymentStatus.fromString(paymentStatusStr);
            ShippingStatus shippingStatus = ShippingStatus.fromString(shippingStatusStr);

            boolean updated = adminOrderService.updateOrderStatus(orderId, paymentStatus, shippingStatus);

            if (updated) {
                resp.sendRedirect("/admin/orders?success=1");
            } else {
                resp.sendRedirect("/admin/orders?error=1");
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        } catch (IllegalArgumentException e) {
            // Nếu enum không hợp lệ
            resp.sendRedirect("/admin/orders?error=invalid_status");
        }
    }
}
