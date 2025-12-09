package com.example.Controller.User.Home;

import com.example.DTO.Products.GetRandomProductResponseDTO;
import com.example.Service.Product.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/home")
public class Home extends HttpServlet {
    public ProductService productService;

    @Override
    public void init() throws ServletException {
        productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        /**
         * Get random products
         */
        List<GetRandomProductResponseDTO> randomProducts = this.productService.getRandomProducts(6);
//        randomProducts.forEach(product -> System.out.println(product.getImageUrl()));
//        randomProducts.forEach(product -> System.out.println("Price: " + product.getPrice() + " discount: " + product.getDiscount()));

        /**
         * Loading brands for ui
         */
        List<Map<String, String>> brands = new ArrayList<>();

        brands.add(Map.of(
                "id", "3",
                "name", "Oppo",
                "image", "https://demobile.vn/wp-content/uploads/2024/10/oppo-find-x7-ultra-vang.jpg"
        ));

        brands.add(Map.of(
                "id", "1",
                "name", "Samsung",
                "image", "https://images.samsung.com/is/image/samsung/assets/vn/2501/pcd/smartphones/PCD_P3_Main-KV_720x1080_mo.jpg"
        ));

        brands.add(Map.of(
                "id", "2",
                "name", "Apple",
                "image", "https://www.apple.com/v/iphone/home/cg/images/overview/select/iphone_17pro__0s6piftg70ym_large.jpg"
        ));


        /**
         * Set attribute to request
         */
        req.setAttribute("brands", brands);
        req.setAttribute("randomProducts", randomProducts);

        /**
         * forward to index jsp
         */
        req.getRequestDispatcher("/user/index.jsp").forward(req, resp);
    }
}