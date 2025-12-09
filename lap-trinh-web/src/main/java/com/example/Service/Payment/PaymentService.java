package com.example.Service.Payment;

import com.example.DAO.PaymentDAO;
import com.example.Model.Payment;
import com.example.Model.PaymentStatus;

import java.sql.Connection;
import java.sql.SQLException;

public class PaymentService {

    private final PaymentDAO paymentDAO;

    public PaymentService(Connection conn) {
        this.paymentDAO = new PaymentDAO(conn);
    }

    public int createPayment(Payment payment) {
        try {
            return paymentDAO.createPayment(payment);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Payment getPaymentById(int id) {
        try {
            return paymentDAO.getPaymentById(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int getUserIdByPaymentId(int paymentId) {
        try {
            return paymentDAO.getUserIdByPaymentId(paymentId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePaymentStatus(int paymentId, PaymentStatus status) {
        try {
            paymentDAO.updatePaymentStatus(paymentId, status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateTransactionId(int paymentId, String transactionId) {
        try {
            paymentDAO.updateTransactionId(paymentId, transactionId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Payment getPaymentByTransactionId(String transactionId) {
        try {
            return paymentDAO.getPaymentByTransactionId(transactionId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Payment getPaymentByOrderId(int orderId) {
        try {
            return paymentDAO.getPaymentByOrderId(orderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
