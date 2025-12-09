package com.example.Controller.User.Payment;

import com.example.Service.Payment.VnPayService;
import com.example.Utils.TokenStorage;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/payment/vnpay")
public class VNPayPaymentController extends HttpServlet {

    private VnPayService vnPayService = new VnPayService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {

        int orderId = Integer.parseInt(req.getParameter("orderId"));
        long amount = Long.parseLong(req.getParameter("amount"));

        String ipAddress = req.getRemoteAddr();

        // Tạo token map với userId
        HttpSession session = req.getSession();
        Object userId = session.getAttribute("userId");
        String token = UUID.randomUUID().toString();
        TokenStorage.put(token, userId);

        // Tạo returnUrl gắn token
        String returnUrl = req.getRequestURL().toString()
                .replace("vnpay", "vnpay-return")
                + "?token=" + token;

        try {
            String paymentUrl = vnPayService.generatePaymentUrl(orderId, amount, returnUrl, ipAddress);
            resp.sendRedirect(paymentUrl);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
