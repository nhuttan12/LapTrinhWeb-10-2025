package com.example.Service.Admin;

import com.example.DAO.AdminProductDAO;
import com.example.DTO.Products.GetProductsPagingResponseDTO;
import com.example.Mappers.ProductMapper;
import com.example.Model.Product;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminProductService {
    private final AdminProductDAO productDAO;

    public AdminProductService(AdminProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    /**
     * 🔹 Lấy danh sách sản phẩm có phân trang
     */
    public List<GetProductsPagingResponseDTO> getProductsPaginated(int page, int pageSize) throws SQLException {
        int offset = (page - 1) * pageSize;
        List<Product> products = productDAO.getAllProductsPaginated(offset, pageSize);
        return ProductMapper.INSTANCE.toGetProductsPagingResponseDTOList(products);
    }


    /**
     * 🔹 Lấy thông tin chi tiết sản phẩm theo ID
     */
    public Product getProductById(int id) throws SQLException {
        return productDAO.getProductById(id);
    }

    /**
     * 🔹 Tạo mới sản phẩm
     */
    public boolean createProduct(Product product) throws SQLException {
        return productDAO.createProduct(product);
    }

    /**
     * 🔹 Cập nhật sản phẩm
     */
    public boolean updateProduct(Product product) throws SQLException {
        return productDAO.updateProduct(product);
    }

    /**
     * 🔹 Xóa mềm (soft delete)
     */
    public boolean softDeleteProduct(int id) throws SQLException {
        return productDAO.softRemoveProduct(id);
    }
}
