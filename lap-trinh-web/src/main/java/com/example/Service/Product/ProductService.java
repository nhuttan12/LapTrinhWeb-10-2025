package com.example.Service.Product;

import com.example.DAO.ProductDAO;
import com.example.DTO.Common.PagingResponse;
import com.example.DTO.Common.SortDirection;
import com.example.DTO.Products.GetProductDetailResponseDTO;
import com.example.DTO.Products.GetProductSameBrandDTO;
import com.example.DTO.Products.GetProductsPagingResponseDTO;
import com.example.DTO.Products.GetRandomProductResponseDTO;
import com.example.Utils.AnalyzeDescription;
import com.example.Mappers.ProductMapper;
import com.example.Model.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class ProductService {
    private final ProductDAO productDAO;
    private final ProductMapper productMapper;
    private final AnalyzeDescription analyzeDescription;

    public ProductService() {
        this.productDAO = new ProductDAO();
        this.analyzeDescription = new AnalyzeDescription();
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
//            System.out.println("Double check get product list by brand id: " + responseDTO.get(0).toString());

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
            int[] ramList,
            int[] storageList,
            int[] chargeList,
            int minPrice,
            int maxPrice
    ) {
        /**
         * Calculate offset and split price range into min price and max price
         */
        int offset = (page - 1) * pageSize;

        PagingResponse<Product> products = productDAO.getProductsByFilterCriteriaPaging(
                offset,
                page,
                pageSize,
                sortBy,
                sortDirections,
                osList,
                ramList,
                storageList,
                chargeList,
                minPrice,
                maxPrice
        );
//        System.out.println("Double check product filter by criteria paging: " + products.toString());

        List<GetProductsPagingResponseDTO> responseDTO = productMapper
                .toGetProductsPagingResponseDTOList(products.getItems());

        return PagingResponse.<GetProductsPagingResponseDTO>builder()
                .items(responseDTO)
                .meta(products.getMeta())
                .build();
    }

    public GetProductDetailResponseDTO getProductDetailByProductId(int productId) {
        try {
            /**
             * Get product detail by product id
             */
            Product product = this.productDAO.getProductDetailByProductId(productId);
//            System.out.println("Double check detail image");
//            product.getProductImages().forEach(productImage -> System.out.println(productImage.getImage().getUrl()));

            /**
             * Analyzing description
             */
            Map<String, String> analyzeDescription = this.analyzeDescription.analyzeDescription(
                    product.getProductDetail().getDescription()
            );

            /**
             * Mapping product to product detail response dto
             */
            GetProductDetailResponseDTO productDetailResponseDTO = this.productMapper.toGetProductDetailResponseDTO(product);

            /**
             * Mapping to new description
             */
            productDetailResponseDTO.setDescription(analyzeDescription);

            return productDetailResponseDTO;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<GetProductSameBrandDTO> getProductByBrandName(String brandName) {
        List<Product> products = this.productDAO.getProductListByBrandName(brandName);

        return this.productMapper.toGetProductSameBrandDTOList(products);
    }

    public PagingResponse<GetProductsPagingResponseDTO> getProductListByProductName(
            String productName,
            int page,
            int pageSize,
            List<String> sortBy,
            List<SortDirection> sortDirections
    ) {
        int offset = (page - 1) * pageSize;

        PagingResponse<Product> products = null;
        try {
            products = productDAO.getProductListByProductName(
                    productName,
                    offset,
                    page,
                    pageSize,
                    sortBy,
                    sortDirections);
//            System.out.println("Double check product: " + products.getItems().get(0));

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

    public List<GetRandomProductResponseDTO> getRandomProducts(int limit) {
        List<Product> products = this.productDAO.getRandomProducts(limit);
//        System.out.println("Double check get random products: ");
//        products.forEach(product -> System.out.println("Price: " + product.getPrice() + " discount: " + product.getDiscount()));

        return this.productMapper.toGetRandomProductResponseDTOList(products);
    }
}
