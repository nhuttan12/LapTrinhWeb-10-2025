package com.example.Service.Order;


import com.example.DAO.OrderDAO;
import com.example.DTO.Orders.OrderUserResponseDTO;
import com.example.Mappers.OrderMapper;
import com.example.Model.Order;
import com.example.Model.OrderStatus;
import com.example.Model.ShippingStatus;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO;
    private final OrderMapper orderMapper;

    public OrderService(Connection conn) {
        this.orderDAO = new OrderDAO(conn);
        this.orderMapper = OrderMapper.INSTANCE;
    }

    public List<OrderUserResponseDTO> findOrdersByUserId(
            int userId,
            int page,
            int pageSize) {
            int offset = (page - 1) * pageSize;
        List<Order> orders = null;
        try {
            orders = orderDAO.findOrdersByUserIdPaging(userId, offset, pageSize);

            return orderMapper.toOrderUserResponseDTOList(orders);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public Order findOrderById(int orderId) {
        try {
            return orderDAO.findOrderById(orderId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public ShippingStatus getShippingStatus(int orderId) {
        try {
            String statusStr = orderDAO.getShippingStatus(orderId); // mới: trả về shipping_status
            return ShippingStatus.fromString(statusStr);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean cancelOrder(int orderId, int userId) {
        try {
            return orderDAO.cancelOrder(orderId, userId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
