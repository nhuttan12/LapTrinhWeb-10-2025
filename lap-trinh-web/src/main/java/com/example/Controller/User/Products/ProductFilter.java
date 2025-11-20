package com.example.Controller.User.Products;

import com.example.Model.FilterCriteria;
import com.example.Utils.FilterUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebServlet("/product-filter")
public class ProductFilter extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /**
         * Extract filter criteria from request use for filter
         */
        FilterCriteria criteria = FilterUtils.extractFilterCriteria(req);
//        System.out.println(criteria);

        req.setAttribute("criteria", criteria);
        req.getRequestDispatcher("/product-list").forward(req, resp);
    }
}
