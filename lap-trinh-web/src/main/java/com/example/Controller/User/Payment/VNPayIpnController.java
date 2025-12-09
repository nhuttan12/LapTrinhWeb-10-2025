package com.example.Controller.User.Payment;

import com.example.Model.PaymentStatus;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.Order.OrderServiceSimple;
import com.example.Service.Payment.PaymentService;
import com.example.Service.Payment.VnPayService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

@WebServlet("/payment/vnpay-ipn")
public class VNPayIpnController extends HttpServlet {

    private PaymentService paymentService;

    @Override
    public void init() {
        try {
            Connection conn = JDBCConnection.getConnection();
            paymentService = new PaymentService(conn);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi khởi tạo VNPayIPNController", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String txnRef = req.getParameter("vnp_TxnRef");
        String responseCode = req.getParameter("vnp_ResponseCode");

        if (txnRef != null) {
            int paymentId = Integer.parseInt(txnRef);
            if ("00".equals(responseCode)) {
                paymentService.updatePaymentStatus(paymentId, PaymentStatus.COMPLETED);
            } else {
                paymentService.updatePaymentStatus(paymentId, PaymentStatus.FAILED);
            }
        }

        // VNPAY yêu cầu response text
        resp.getWriter().write("OK");
    }
}

