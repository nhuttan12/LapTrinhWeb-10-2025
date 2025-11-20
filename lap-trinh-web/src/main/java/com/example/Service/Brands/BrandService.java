package com.example.Service.Brands;

import com.example.DAO.BrandDAO;
import com.example.DTO.Brands.GetBrandResponseDTO;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class BrandService {
    private final BrandDAO brandDAO;

    public BrandService() {
        this.brandDAO = new BrandDAO();
    }

    public List<GetBrandResponseDTO> getBrandsWithProductCount() {
        try {
            return brandDAO.getBrandsWithProductCount();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
