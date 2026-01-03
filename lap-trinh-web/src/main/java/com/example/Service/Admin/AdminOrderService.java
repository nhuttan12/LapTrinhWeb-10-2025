package com.example.Service.Admin;

import com.example.DAO.AdminOrderDAO;
import com.example.DTO.Common.PagingResponse;
import com.example.DTO.Orders.GetOrdersPagingResponseAdminDTO;
import com.example.Mappers.OrderMapper;
import com.example.Model.Order;
import com.example.Model.PaymentStatus;
import com.example.Model.ShippingStatus;

import java.sql.SQLException;
import java.util.List;

public class AdminOrderService {
    private final AdminOrderDAO adminOrderDAO;
    private final OrderMapper orderMapper;

    public AdminOrderService(AdminOrderDAO adminOrderDAO) {
        this.adminOrderDAO = adminOrderDAO;
        this.orderMapper = OrderMapper.INSTANCE;
    }

    public PagingResponse<GetOrdersPagingResponseAdminDTO> getAllOrderPaging(int page, int pageSize) throws SQLException {
        int offset = (page - 1) * pageSize;

        PagingResponse<Order> orderPagingResponse = adminOrderDAO.getAllOrders(offset, page, pageSize);

        List<GetOrdersPagingResponseAdminDTO> mappedOrders =
                this.orderMapper
                        .toGetOrdersPagingResponseAdminDTOList(orderPagingResponse.getItems());

        return PagingResponse.<GetOrdersPagingResponseAdminDTO>builder()
                .items(mappedOrders)
                .meta(orderPagingResponse.getMeta())
                .build();

    }

    public Order getOrderById(int orderId) throws SQLException {
        return adminOrderDAO.getOrderById(orderId);
    }

    public boolean updateOrderStatus(int orderId, PaymentStatus paymentStatus, ShippingStatus shippingStatus) throws SQLException {
        return adminOrderDAO.updateOrderStatus(orderId, paymentStatus, shippingStatus);
    }
}
