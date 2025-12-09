package com.example.Service.Order;

import com.example.DAO.OrderDetailDAO;
import com.example.DTO.Orders.OrderDetailUserResponseDTO;
import com.example.Mappers.OrderDetailMapper;
import com.example.Model.OrderDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailService {

    private final OrderDetailDAO orderDetailDAO;
    private final OrderDetailMapper orderDetailMapper;

    public OrderDetailService(Connection conn) {
        this.orderDetailDAO = new OrderDetailDAO(conn);
        this.orderDetailMapper = OrderDetailMapper.INSTANCE;
    }

    public List<OrderDetailUserResponseDTO> getOrderDetailByOrderId(int orderId, int userId) {
        try {
            List<OrderDetail> list = orderDetailDAO.getOrderDetails(orderId, userId);
            return orderDetailMapper.toOrderDetailUserResponseDTOList(list);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
