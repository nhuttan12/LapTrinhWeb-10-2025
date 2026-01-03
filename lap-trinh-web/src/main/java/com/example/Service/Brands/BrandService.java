package com.example.Service.Brands;

import com.example.DAO.BrandDAO;
import com.example.DTO.Brands.GetBrandResponseDTO;
import com.example.Model.Brand;

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

    public Brand getBrandByName(String name) {
        try {
            return brandDAO.getBrandByName(name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Brand createBrandByBrandName(String brandName) {
        try {
            boolean result = brandDAO.createBrandWithBrandName(brandName);

            if (result) return getBrandByName(brandName);
            else return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
