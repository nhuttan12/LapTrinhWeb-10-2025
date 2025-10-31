package com.example.Filter;

import com.example.DTO.Brands.GetBrandResponseDTO;
import com.example.Service.Brands.BrandService;
import com.example.Service.Database.JDBCConnection;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@WebFilter(urlPatterns = {
        "/home",
        "/product-list",
        "/about",
        "/contact",
        "/product-detail",
        "/cart",
        "/checkout",
        "/profile",
        "/order",
        "/"
})
public class HeaderDataFilter implements Filter {
    private BrandService brandService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            Connection conn = JDBCConnection.getConnection();
            brandService = new BrandService(conn);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            List<GetBrandResponseDTO> brands = brandService.getBrandsWithProductCount();

            request.setAttribute("brands", brands);
        } catch (Exception e) {
            e.printStackTrace();
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

}
