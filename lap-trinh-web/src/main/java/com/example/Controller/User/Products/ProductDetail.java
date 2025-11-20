package com.example.Controller.User.Products;

import com.example.DTO.Products.GetProductDetailResponseDTO;
import com.example.DTO.Products.GetProductSameBrandDTO;
import com.example.Helper.AnalyzeSpecs;
import com.example.Model.Product;
import com.example.Service.Product.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@WebServlet("/product-detail")
public class ProductDetail extends HttpServlet {
    private ProductService productService;
    private AnalyzeSpecs analyzeSpecs;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
        analyzeSpecs = new AnalyzeSpecs();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int productId = Integer.parseInt(Optional.ofNullable(req.getParameter("productId")).orElse("-1"));

        if (productId > 0) {
            GetProductDetailResponseDTO detailResponseDTO = productService.getProductDetailByProductId(productId);

            /**
             * Get product by brand get from detailResponseDTO
             */
            List<GetProductSameBrandDTO> sameBrandProducts = productService.getProductByBrandName(detailResponseDTO.getBrandName());

            Map<String, Object> specs = analyzeSpecs.analyzeSpecs(detailResponseDTO);

            req.setAttribute("specs", specs);
            req.setAttribute("detail", detailResponseDTO);
            req.setAttribute("sameBrandProducts", sameBrandProducts);

            // forward sang file JSP
            req.getRequestDispatcher("/user/product-detail.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("admin/pages/samples/error-404.jsp").forward(req, resp);
        }
    }
}