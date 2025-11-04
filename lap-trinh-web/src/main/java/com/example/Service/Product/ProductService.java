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

    public ProductService() {
        this.productDAO = new ProductDAO();
        this.productMapper = ProductMapper.INSTANCE;
    }

    public PagingResponse<GetProductsPagingResponseDTO> getProductsPaging(
            int page,
            int pageSize,
            List<String> sortBy,
            List<SortDirection> sortDirections
    ) {
        int offset = (page - 1) * pageSize;

        PagingResponse<Product> products = null;
        try {
            products = productDAO.getProductsPaging(
                    offset,
                    page,
                    pageSize,
                    sortBy,
                    sortDirections);

            List<GetProductsPagingResponseDTO> responseDTO = productMapper
                    .toGetProductsPagingResponseDTOList(products.getItems());

            return PagingResponse.<GetProductsPagingResponseDTO>builder()
                    .items(responseDTO)
                    .meta(products.getMeta())
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PagingResponse<GetProductsPagingResponseDTO> getNewProductsPaging(
            int page,
            int pageSize,
            List<String> sortBy,
            List<SortDirection> sortDirections
    ) {
        int offset = (page - 1) * pageSize;

        PagingResponse<Product> products = null;
        try {
            products = productDAO.getNewProductsPaging(
                    offset,
                    page,
                    pageSize,
                    sortBy,
                    sortDirections);

            List<GetProductsPagingResponseDTO> responseDTO = productMapper
                    .toGetProductsPagingResponseDTOList(products.getItems());

            return PagingResponse.<GetProductsPagingResponseDTO>builder()
                    .items(responseDTO)
                    .meta(products.getMeta())
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PagingResponse<GetProductsPagingResponseDTO> getProductsPagingByBrandId(
            int brandId,
            int page,
            int pageSize,
            List<String> sortBy,
            List<SortDirection> sortDirections
    ) {
        int offset = (page - 1) * pageSize;

        PagingResponse<Product> products = null;
        try {
            products = productDAO.getProductsPagingByBrandId(
                    brandId,
                    offset,
                    page,
                    pageSize,
                    sortBy,
                    sortDirections);

            List<GetProductsPagingResponseDTO> responseDTO = productMapper
                    .toGetProductsPagingResponseDTOList(products.getItems());

            return PagingResponse.<GetProductsPagingResponseDTO>builder()
                    .items(responseDTO)
                    .meta(products.getMeta())
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product getProductById(int id) {
        try {
            return productDAO.getProductByProductId(id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PagingResponse<GetProductsPagingResponseDTO> getProductsByFilterCriteriaPaging(
            int page,
            int pageSize,
            List<String> sortBy,
            List<SortDirection> sortDirections,
            String[] osList,
            String[] ramList,
            String[] storageList,
            String[] chargeList
    ) {
        int offset = (page - 1) * pageSize;

        PagingResponse<Product> products = productDAO.getProductsByFilterCriteriaPaging(
                offset, page, pageSize, sortBy, sortDirections, osList, ramList, storageList, chargeList
        );

        List<GetProductsPagingResponseDTO> responseDTO = productMapper
                .toGetProductsPagingResponseDTOList(products.getItems());

        return PagingResponse.<GetProductsPagingResponseDTO>builder()
                .items(responseDTO)
                .meta(products.getMeta())
                .build();
    }
}
