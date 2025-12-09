package com.example.Service.Admin;

import com.example.DAO.AdminOrderDAO;
import com.example.Model.Order;

import java.sql.SQLException;
import java.util.List;

public class AdminOrderService {
    private final AdminOrderDAO adminOrderDAO;
    public AdminOrderService(AdminOrderDAO adminOrderDAO){
         this.adminOrderDAO = adminOrderDAO;
    }
    public List<Order> getAllOrders(int page, int pageSize) throws SQLException {
        return adminOrderDAO.getAllOrders(page, pageSize);
    }

    public Order getOrderById(int orderId) throws SQLException {
        return adminOrderDAO.getOrderById(orderId);
    }

    public boolean updateOrderStatus(int orderId, String status) throws SQLException {
        return adminOrderDAO.updateOrderStatus(orderId, status);
    }

}
