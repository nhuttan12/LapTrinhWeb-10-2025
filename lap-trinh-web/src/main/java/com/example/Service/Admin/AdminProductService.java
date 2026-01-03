package com.example.Service.Admin;

import com.example.DAO.AdminProductDAO;
import com.example.DTO.Products.CreateProductRequestDTO;
import com.example.DTO.Products.GetProductsPagingResponseDTO;
import com.example.DTO.Products.UpdateProductRequestDTO;
import com.example.Mappers.ProductMapper;
import com.example.Model.Brand;
import com.example.Model.ImageType;
import com.example.Model.Product;
import com.example.Model.ProductDetail;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AdminProductService {
    private final AdminProductDAO productDAO;
    private ProductMapper mapper;

    public AdminProductService(AdminProductDAO productDAO) {
        this.productDAO = productDAO;
        this.mapper = ProductMapper.INSTANCE;
    }

    public List<GetProductsPagingResponseDTO> getProductsPaging(int page, int limit) throws SQLException {
        int offset = (page - 1) * limit;
        return productDAO.getProductsPaging(offset, limit);
    }

    public int getTotalPages(int limit) throws SQLException {
        int total = productDAO.countAllProducts();
        return (int) Math.ceil((double) total / limit);
    }

    // create product + detail + thumbnail (if dto.thumbnailUrl != null)
    public void createProduct(CreateProductRequestDTO dto) throws SQLException {
        Connection conn = productDAO.getConnection();
        boolean previousAutoCommit = true;
        try {
            previousAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            Product p = mapper.toProductEntity(dto);
            ProductDetail d = mapper.toProductDetailEntity(dto);

            int productId = productDAO.insertProduct(p);
            d.setProductId(productId);
            productDAO.insertProductDetail(d);

            // thumbnail
            if (dto.getThumbnail() != null && !dto.getThumbnail().isBlank()) {
                int imageId = productDAO.insertImage(dto.getThumbnail());
                productDAO.insertProductImage(productId, imageId, ImageType.THUMBNAIL);
            }

            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(previousAutoCommit);
        }
    }

    // helper để load product + detail
    public Product getProductById(int id) throws SQLException {
        return productDAO.getProductById(id);
    }

    public ProductDetail getProductDetailByProductId(int id) throws SQLException {
        return productDAO.getProductDetailByProductId(id);
    }

    // update product + detail, nếu dto chứa thumbnailUrl thì thay thumbnail cũ
    public void updateProduct(Product existing, ProductDetail detail, UpdateProductRequestDTO dto) throws SQLException {
        Connection conn = productDAO.getConnection();
        boolean previousAutoCommit = true;
        try {
            previousAutoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);

            // update entities via mapper
            mapper.updateProductFromDto(dto, existing);
            mapper.updateProductDetailFromDto(dto, detail);
            detail.setProductId(existing.getId());

            productDAO.updateProduct(existing);
            productDAO.updateProductDetail(detail);

            // nếu client gửi thumbnailUrl -> thay thumbnail
            if (dto.getThumbnail() != null && !dto.getThumbnail().isBlank()) {
                // xóa product_images type=THUMBNAIL (chỉ mapping)
                productDAO.deleteProductImagesByType(existing.getId(), ImageType.THUMBNAIL);

                // insert new image + mapping
                int imageId = productDAO.insertImage(dto.getThumbnail());
                productDAO.insertProductImage(existing.getId(), imageId, ImageType.THUMBNAIL);
            }

            conn.commit();
        } catch (Exception ex) {
            conn.rollback();
            throw ex;
        } finally {
            conn.setAutoCommit(previousAutoCommit);
        }
    }

    public void softDelete(int id) throws SQLException {
        productDAO.softDelete(id);
    }

}
