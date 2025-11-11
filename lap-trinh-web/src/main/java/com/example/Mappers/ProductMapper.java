package com.example.Mappers;

        import com.example.DTO.Products.GetProductsPagingResponseDTO;
        import com.example.Model.Product;
        import org.mapstruct.Mapper;
        import org.mapstruct.Mapping;
        import org.mapstruct.factory.Mappers;

        import java.util.List;

@Mapper()
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "discount", target = "discount")
    @Mapping(source = "productDetail.description", target = "description")
    @Mapping(source = "productImage.image.url", target = "thumbnail")
    GetProductsPagingResponseDTO toGetProductsPagingResponseDTO(Product product);

    List<GetProductsPagingResponseDTO> toGetProductsPagingResponseDTOList(List<Product> products);
}
