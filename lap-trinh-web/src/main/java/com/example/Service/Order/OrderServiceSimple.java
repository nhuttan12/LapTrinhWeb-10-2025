package com.example.Service.Order;

import com.example.DAO.OrderDAO;
import com.example.Model.Cart;
import com.example.Model.PaymentStatus;
import com.example.Model.ShippingStatus;

import java.sql.Connection;
import java.sql.SQLException;

public class OrderServiceSimple {

    private final OrderDAO orderDAO;

    public OrderServiceSimple(Connection conn) {
        this.orderDAO = new OrderDAO(conn);
    }

    public void updatePaymentStatus(int orderId, PaymentStatus status) {
        try {
            orderDAO.updatePaymentStatus(orderId, status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateShippingStatus(int orderId, ShippingStatus status) {
        try {
            orderDAO.updateShippingStatus(orderId, status);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public int createOrderFromCart(Cart cart) {
        try {
            return orderDAO.createOrderFromCart(cart);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
