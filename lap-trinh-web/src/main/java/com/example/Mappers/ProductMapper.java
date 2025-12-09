package com.example.Mappers;

import com.example.DTO.Products.*;
import com.example.Model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper()
public interface ProductMapper {
    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    default String getFirstThumbnail(List<ProductImage> images) {
        if (images == null) return null;
        return images.stream()
                .filter(img -> img.getType() == ImageType.THUMBNAIL)
                .map(img -> img.getImage() != null ? img.getImage().getUrl() : null)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    default List<String> getDetailImageUrls(List<ProductImage> images) {
        if (images == null) return Collections.emptyList();
        return images.stream()
                .filter(img -> img.getType() == ImageType.GALLERY && img.getImage() != null)
                .map(img -> img.getImage().getUrl())
                .collect(Collectors.toList());
    }

    /**
     * Product mapping
     */
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "price", target = "price")
    @Mapping(source = "discount", target = "discount")
    @Mapping(source = "productDetail.brand.name", target = "brand")
    @Mapping(expression = "java(getFirstThumbnail(product.getProductImages()))", target = "thumbnail")
    GetProductsPagingResponseDTO toGetProductsPagingResponseDTO(Product product);

    List<GetProductsPagingResponseDTO> toGetProductsPagingResponseDTOList(List<Product> products);

    /**
     * Product detail mapping
     */
    @Mapping(source = "product.id", target = "id")
    @Mapping(source = "product.name", target = "name")
    @Mapping(source = "product.price", target = "price")
    @Mapping(source = "product.discount", target = "discount")

    // ProductDetail
    @Mapping(source = "product.productDetail.os", target = "os")
    @Mapping(source = "product.productDetail.ram", target = "ram")
    @Mapping(source = "product.productDetail.storage", target = "storage")
    @Mapping(source = "product.productDetail.batteryCapacity", target = "batteryCapacity")
    @Mapping(source = "product.productDetail.screenSize", target = "screenSize")
    @Mapping(source = "product.productDetail.screenResolution", target = "screenResolution")
    @Mapping(source = "product.productDetail.mobileNetwork", target = "mobileNetwork")
    @Mapping(source = "product.productDetail.cpu", target = "cpu")
    @Mapping(source = "product.productDetail.gpu", target = "gpu")
    @Mapping(source = "product.productDetail.waterResistance", target = "waterResistance")
    @Mapping(source = "product.productDetail.maxChargeWatt", target = "maxChargeWatt")
    @Mapping(source = "product.productDetail.design", target = "design")
    @Mapping(source = "product.productDetail.memoryCard", target = "memoryCard")
    @Mapping(source = "product.productDetail.cpuSpeed", target = "cpuSpeed")
    @Mapping(source = "product.productDetail.releaseDate", target = "releaseDate")
    @Mapping(source = "product.productDetail.rating", target = "rating")
    @Mapping(target = "description", ignore = true)

    // Brand
    @Mapping(source = "product.productDetail.brand.name", target = "brandName")

    // Images
    @Mapping(expression = "java(getFirstThumbnail(product.getProductImages()))", target = "thumbnailImages")
    @Mapping(expression = "java(getDetailImageUrls(product.getProductImages()))", target = "detailImages")
    GetProductDetailResponseDTO toGetProductDetailResponseDTO(Product product);

    List<GetProductDetailResponseDTO> toGetProductDetailResponseDTOList(List<Product> products);

    /**
     * Product same brand mapping
     */
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    @Mapping(expression = "java(getFirstThumbnail(product.getProductImages()))", target = "imageUrl")
    GetProductSameBrandDTO toGetProductSameBrand(Product product);

    List<GetProductSameBrandDTO> toGetProductSameBrandDTOList(List<Product> products);

    /**
     * Random product mapping
     */
    @Mapping(target = "imageUrl", expression = "java(getFirstThumbnail(product.getProductImages()))")
    GetRandomProductResponseDTO toGetRandomProductResponseDTO(Product product);

    List<GetRandomProductResponseDTO> toGetRandomProductResponseDTOList(List<Product> products);

    // Create product mapper
    @Mapping(target = "id", ignore = true)
    @Mapping(source = "status", target = "status")
    Product toProductEntity(CreateProductRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "productId", ignore = true)
    @Mapping(target = "brand", expression = "java(createBrand(dto.getBrandId()))")
    ProductDetail toProductDetailEntity(CreateProductRequestDTO dto);

    // Upload product mapper
    void updateProductFromDto(UpdateProductRequestDTO dto, @MappingTarget Product product);

    @Mapping(target = "brand", expression = "java(createBrand(dto.getBrandId()))")
    void updateProductDetailFromDto(UpdateProductRequestDTO dto, @MappingTarget ProductDetail detail);

    /* BRAND HELPER */
    default Brand createBrand(Integer id) {
        if (id == null) return null;
        return Brand.builder().id(id).build();
    }
}
