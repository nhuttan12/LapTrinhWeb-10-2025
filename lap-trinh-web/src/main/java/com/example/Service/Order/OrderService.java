package com.example.Service.Order;


import com.example.DAO.OrderDAO;
import com.example.DTO.Order.OrderUserResponseDTO;
import com.example.Mappers.OrderMapper;
import com.example.Model.Order;

import java.sql.Connection;
import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO;
    private final OrderMapper orderMapper;

    public OrderService(Connection conn) {
        this.orderDAO = new OrderDAO(conn);
        this.orderMapper = OrderMapper.INSTANCE;
    }

    public List<OrderUserResponseDTO> findOrdersByUserId(int userId, int page, int pageSize) {
        List<Order> orders = orderDAO.findOrdersByUserIdPaging(userId, page, pageSize);

        return orderMapper.toOrderUserResponseDTOList(orders);
    }
}
