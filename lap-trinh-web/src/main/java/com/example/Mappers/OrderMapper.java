package com.example.Mappers;

import com.example.DTO.Orders.OrderUserResponseDTO;
import com.example.Model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper()
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mappings({
            @Mapping(target = "id", expression = "java(String.valueOf(order.getId()))"),
            @Mapping(target = "totalPrice", expression = "java(order.getPrice().intValue())"),
            @Mapping(target = "status", expression = "java(order.getStatus().name())"),
            @Mapping(target = "createdAt", expression = "java(order.getCreatedAt().toString())")
    })
    OrderUserResponseDTO toOrderUserResponseDTO(Order order);

    List<OrderUserResponseDTO> toOrderUserResponseDTOList(List<Order> orders);
}