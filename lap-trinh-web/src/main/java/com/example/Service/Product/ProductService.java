package com.example.Service.Product;

import com.example.DAO.ProductDAO;
import com.example.DTO.Common.PagingMetaData;
import com.example.DTO.Common.PagingResponse;
import com.example.DTO.Common.SortDirection;
import com.example.DTO.Products.GetProductsPagingResponseDTO;
import com.example.Mappers.ProductMapper;
import com.example.Model.Product;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductDAO productDAO;
    private final ProductMapper productMapper;

    public ProductService(Connection conn) {
        this.productDAO = new ProductDAO(conn);
        this.productMapper = ProductMapper.INSTANCE;
    }

    public PagingResponse<GetProductsPagingResponseDTO> getProductsPaging(
            int page,
            int pageSize,
            List<String> sortBy,
            List<SortDirection> sortDirections
    ) throws SQLException {
        int offset = (page - 1) * pageSize;

        PagingMetaData meta = PagingMetaData.builder()
                .currentPage(page)
                .pageSize(pageSize)
                .sortBy(sortBy)
                .sortDirections(sortDirections)
                .build();

        PagingResponse<Product> products = productDAO.getProductsPaging(meta, offset);

        List<GetProductsPagingResponseDTO> responseDTO = productMapper
                .toGetProductsPagingResponseDTOList(products.getItems());

        return PagingResponse.<GetProductsPagingResponseDTO>builder()
                .items(responseDTO)
                .meta(products.getMeta())
                .build();
    }

    public PagingResponse<GetProductsPagingResponseDTO> getNewProductsPaging(
            int page,
            int pageSize
    ) throws SQLException {
        int offset = (page - 1) * pageSize;

        PagingMetaData meta = PagingMetaData.builder()
                .currentPage(page)
                .pageSize(pageSize)
                .sortBy(List.of("created_at"))
                .sortDirections(List.of(SortDirection.DESC))
                .build();

        PagingResponse<Product> products = productDAO.getNewProductsPaging(meta, offset);

        List<GetProductsPagingResponseDTO> responseDTO = productMapper
                .toGetProductsPagingResponseDTOList(products.getItems());

        return PagingResponse.<GetProductsPagingResponseDTO>builder()
                .items(responseDTO)
                .meta(products.getMeta())
                .build();
    }
}
