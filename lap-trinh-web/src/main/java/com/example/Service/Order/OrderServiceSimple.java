package com.example.Service.Order;

import com.example.DAO.OrderDAO;
import com.example.Model.Cart;

import java.sql.Connection;

public class OrderServiceSimple {

    private final OrderDAO orderDAO;

    public OrderServiceSimple(Connection conn) {
        this.orderDAO = new OrderDAO(conn);
    }

    public void updateOrderStatus(int orderId, String status) {
        try {
            orderDAO.updateOrderStatus(orderId, status);
        } catch (Exception e) {
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
