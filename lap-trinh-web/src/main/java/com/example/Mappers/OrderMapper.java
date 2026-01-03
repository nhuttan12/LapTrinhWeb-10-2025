package com.example.Mappers;

import com.example.DTO.Orders.GetOrdersPagingResponseAdminDTO;
import com.example.DTO.Orders.OrderUserResponseDTO;
import com.example.Model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper()
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Named("timestampToString")
    default String timestampToString(Timestamp timestamp) {
        if (timestamp == null) return null;
        return timestamp.toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    @Named("priceToString")
    default String priceToString(Double price) {
        if (price == null) return "0";
        return String.format("%,.0f", price);
    }

    @Mappings({
            @Mapping(target = "id", expression = "java(String.valueOf(order.getId()))"),
            @Mapping(target = "totalPrice", expression = "java(order.getPrice().intValue())"),
            @Mapping(target = "paymentStatus", expression = "java(order.getPaymentStatus())"),
            @Mapping(target = "shippingStatus", expression = "java(order.getShippingStatus())"),
            @Mapping(target = "createdAt", expression = "java(order.getCreatedAt())")
    })
    OrderUserResponseDTO toOrderUserResponseDTO(Order order);

    List<OrderUserResponseDTO> toOrderUserResponseDTOList(List<Order> orders);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "price", target = "price", qualifiedByName = "priceToString")
    @Mapping(source = "paymentStatus", target = "paymentStatus")
    @Mapping(source = "shippingStatus", target = "shippingStatus")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "timestampToString")
    @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "timestampToString")
    GetOrdersPagingResponseAdminDTO toGetOrdersPagingResponseAdminDTO(Order order);

    List<GetOrdersPagingResponseAdminDTO>
    toGetOrdersPagingResponseAdminDTOList(List<Order> orders);
}