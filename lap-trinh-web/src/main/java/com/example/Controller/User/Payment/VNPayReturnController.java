package com.example.Controller.User.Payment;

import com.example.Model.Payment;
import com.example.Model.PaymentStatus;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.Order.OrderServiceSimple;
import com.example.Service.Payment.PaymentService;
import com.example.Utils.TokenStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/payment/vnpay-return")
public class VNPayReturnController extends HttpServlet {

    private PaymentService paymentService;
    private OrderServiceSimple orderService;
    private Connection conn;

    @Override
    public void init() {
        try {
            conn = JDBCConnection.getConnection();
            paymentService = new PaymentService(conn);
            orderService = new OrderServiceSimple(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        String responseCode = req.getParameter("vnp_ResponseCode");
        int paymentId = Integer.parseInt(req.getParameter("vnp_TxnRef"));

        try {
            // Lấy payment để biết orderId
            Payment payment = paymentService.getPaymentById(paymentId);
            int orderId = payment.getOrderId();

            // Lấy userId từ token
            String token = req.getParameter("token");
            System.out.println("===== VNPayReturnController =====");
            System.out.println("Token từ URL: " + token);
            int userId;
            if (token != null) {
                Object tmp = TokenStorage.get(token);
                if(tmp != null){
                    userId = Integer.parseInt(tmp.toString());
                    // Set session userId
                    req.getSession().setAttribute("userId", userId);
                    // Remove token khỏi map
                    TokenStorage.remove(token);
                } else {
                    // fallback: lấy từ database
                    userId = paymentService.getUserIdByPaymentId(paymentId);
                    req.getSession().setAttribute("userId", userId);
                }
            } else {
                userId = paymentService.getUserIdByPaymentId(paymentId);
                req.getSession().setAttribute("userId", userId);
                System.out.println("Session ID: " + req.getSession().getId());
                System.out.println("Session userId: " + req.getSession().getAttribute("userId"));
            }

            // Cập nhật trạng thái payment và order
            if ("00".equals(responseCode)) {
                paymentService.updatePaymentStatus(paymentId, PaymentStatus.COMPLETED);
                orderService.updateOrderStatus(orderId, "paid");
                resp.sendRedirect(req.getContextPath() + "/payment-success?orderId=" + orderId);
            } else {
                paymentService.updatePaymentStatus(paymentId, PaymentStatus.FAILED);
                resp.sendRedirect(req.getContextPath() + "/payment-failed");
            }

        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/payment-failed");
        }
    }
}
