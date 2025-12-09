package com.example.Controller.User.Payment;

import com.example.Model.Payment;
import com.example.Model.PaymentStatus;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.Order.OrderServiceSimple;
import com.example.Service.Payment.PaymentService;
import com.example.Service.Payment.PaypalClient;
import com.paypal.orders.*;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.io.IOException;
import java.sql.Connection;

@WebServlet("/payment/paypal-return")
public class PaypalReturnController extends HttpServlet {

    private PaymentService paymentService;
    private OrderServiceSimple orderService;

    @Override
    public void init() {
        try {
            Connection conn = JDBCConnection.getConnection();
            paymentService = new PaymentService(conn);
            orderService = new OrderServiceSimple(conn);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String paypalOrderId = req.getParameter("token");

        try {
            // Step 1: capture payment
            OrdersCaptureRequest request = new OrdersCaptureRequest(paypalOrderId);
            request.requestBody(new OrderRequest());
            PaypalClient.client.execute(request);

            // Step 2: lấy payment từ DB theo transaction_id
            Payment payment = paymentService.getPaymentByTransactionId(paypalOrderId);

            if (payment == null) {
                throw new ServletException("ERROR: Payment not found for PayPal token: " + paypalOrderId);
            }

            int paymentId = payment.getId();
            int orderId = payment.getOrderId();

            // Step 3: update DB
            paymentService.updatePaymentStatus(paymentId, PaymentStatus.COMPLETED);
            orderService.updateOrderStatus(orderId, "paid");

            // Step 4: redirect user
            resp.sendRedirect(req.getContextPath() + "/order-detail?orderId=" + orderId);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
