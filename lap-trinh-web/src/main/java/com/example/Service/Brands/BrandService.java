package com.example.Service.Brands;

import com.example.DAO.BrandDAO;
import com.example.DTO.Brands.GetBrandResponseDTO;

import java.sql.Connection;
import java.util.List;

public class BrandService {
    private final BrandDAO brandDAO;

    public BrandService(Connection conn) {
        this.brandDAO = new BrandDAO(conn);
    }

    public List<GetBrandResponseDTO> getBrandsWithProductCount() {
        return brandDAO.getBrandsWithProductCount();
    }
}
