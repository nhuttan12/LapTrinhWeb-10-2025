package com.example.Mappers;

import com.example.DTO.Orders.OrderDetailUserResponseDTO;
import com.example.Model.Image;
import com.example.Model.OrderDetail;
import com.example.Model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper()
public interface OrderDetailMapper {
    OrderDetailMapper INSTANCE = Mappers.getMapper(OrderDetailMapper.class);

    default String getFirstProductImage(Product product) {
        if (product == null || product.getProductImages() == null || product.getProductImages().isEmpty()) {
            return null;
        }
        Image image = product.getProductImages().get(0).getImage();
        return image != null ? image.getUrl() : null;
    }

    @Mapping(target = "productName", source = "product.name")
    @Mapping(target = "productImage", expression = "java(getFirstProductImage(detail.getProduct()))")
    @Mapping(target = "quantity", source = "quantity")
    @Mapping(target = "price", source = "price")
    @Mapping(target = "totalPrice", expression = "java(detail.getPrice() * detail.getQuantity())")
    OrderDetailUserResponseDTO toOrderDetailUserResponseDTO(OrderDetail detail);

    List<OrderDetailUserResponseDTO> toOrderDetailUserResponseDTOList(List<OrderDetail> orderDetails);
}
