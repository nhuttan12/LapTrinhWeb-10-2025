package com.example.Controller.User.ProductList;

import com.example.DTO.Brands.GetBrandResponseDTO;
import com.example.DTO.Common.PagingResponse;
import com.example.DTO.Common.SortDirection;
import com.example.DTO.Products.GetProductsPagingResponseDTO;
import com.example.Model.FilterCriteria;
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
        productService = new ProductService();
        brandService = new BrandService();
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

            boolean newProducts = Optional.ofNullable(req.getParameter("newProduct"))
                    .map(Boolean::parseBoolean)
                    .orElse(false);

            int brandId = Optional.ofNullable(req.getParameter("brandId"))
                    .map(Integer::parseInt)
                    .orElse(0);

            /**
             * Get filter criteria from request product-filter
             */
            FilterCriteria criteria = (FilterCriteria) req.getAttribute("criteria");

            /**
             * Get brands with product count
             */
            List<GetBrandResponseDTO> brands = brandService.getBrandsWithProductCount();

            PagingResponse<GetProductsPagingResponseDTO> response;

            if (brandId >= 1) {
                /**
                 * Get products paging with brand id
                 */
                response = productService.getProductsPagingByBrandId(
                        brandId,
                        page,
                        pageSize,
                        sortBy,
                        sortDirections);

            } else if (newProducts) {
                /**
                 * Get new products
                 */
                response = productService.getNewProductsPaging(page, pageSize, sortBy, sortDirections);

            } else if (criteria != null) {
                /**
                 * Product filtering
                 */
                response = productService.getProductsByFilterCriteriaPaging(
                        page,
                        pageSize,
                        sortBy,
                        sortDirections,
                        criteria.getOsList(),
                        criteria.getRamList(),
                        criteria.getStorageList(),
                        criteria.getChargeList());

                /**
                 * Store the old attributes of filter box to request scope
                 */
                req.setAttribute("criteria", criteria);

            } else {
                /**
                 * Get product list paging
                 */
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
