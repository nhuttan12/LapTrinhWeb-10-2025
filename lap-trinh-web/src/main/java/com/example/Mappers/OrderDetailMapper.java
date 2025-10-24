package com.example.Mappers;

import com.example.DTO.Order.OrderDetailUserResponseDTO;
import com.example.Model.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper()
public interface OrderDetailMapper {
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);

    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productImage", expression = "java(detail.getProduct() != null && detail.getProduct().getProductImage() != null && detail.getProduct().getProductImage().getImage() != null ? detail.getProduct().getProductImage().getImage().getUrl() : null)")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "totalPrice", expression = "java(detail.getPrice() * detail.getQuantity())")
    OrderDetailUserResponseDTO toOrderDetailUserResponseDTO(OrderDetail detail);

    List<OrderDetailUserResponseDTO> toOrderDetailUserResponseDTOList(List<OrderDetail> orderDetails);
}
