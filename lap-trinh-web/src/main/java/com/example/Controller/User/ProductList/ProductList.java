package com.example.Controller.User.ProductList;

import com.example.DTO.Brands.GetBrandResponseDTO;
import com.example.DTO.Common.PagingResponse;
import com.example.DTO.Common.SortDirection;
import com.example.DTO.Products.GetProductsPagingResponseDTO;
import com.example.Service.Auth.AuthService;
import com.example.Service.Brands.BrandService;
import com.example.Service.Database.JDBCConnection;
import com.example.Service.Product.ProductService;
import com.example.Service.User.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet("/product-list")
public class ProductList extends HttpServlet {
    private ProductService productService;
    private BrandService brandService;

    @Override
    public void init() throws ServletException {
        try {
            Connection conn = JDBCConnection.getConnection();
            productService = new ProductService(conn);
            brandService = new BrandService(conn);
        } catch (SQLException e) {
            throw new ServletException("Failed to initialize DB connection", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int page = Optional.ofNullable(req.getParameter("page"))
                    .map(Integer::parseInt)
                    .orElse(1);

            int pageSize = Optional.ofNullable(req.getParameter("pageSize"))
                    .map(Integer::parseInt)
                    .orElse(12);

            boolean newProducts = Optional.ofNullable(req.getParameter("newProduct"))
                    .map(Boolean::parseBoolean)
                    .orElse(false);

            /**
             * Get brands with product count
             */
            List<GetBrandResponseDTO> brands = brandService.getBrandsWithProductCount();

            PagingResponse<GetProductsPagingResponseDTO> response;

            if (newProducts) {
                response = productService.getNewProductsPaging(page, pageSize);

            } else {
                String[] sortByParams = req.getParameterValues("sortBy");
                String[] sortDirParams = req.getParameterValues("sortDir");

                List<String> sortBy = sortByParams != null
                        ? Arrays.asList(sortByParams)
                        : List.of("name");

                List<SortDirection> sortDirections = sortDirParams != null
                        ? Arrays.stream(sortDirParams)
                        .map(dir -> SortDirection.valueOf(dir.toUpperCase()))
                        .collect(Collectors.toList())
                        : List.of(SortDirection.ASC);

                System.out.println("sortBy=" + sortBy + ", sortDirections=" + sortDirections);

                response = productService.getProductsPaging(page, pageSize, sortBy, sortDirections);
            }

            req.setAttribute("products", response.getItems());
            req.setAttribute("meta", response.getMeta());
            req.setAttribute("brands", brands);

            req.getRequestDispatcher("/user/product-list.jsp").forward(req, resp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}