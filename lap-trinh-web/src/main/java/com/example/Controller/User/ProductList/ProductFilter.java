package com.example.Controller.User.ProductList;

import com.example.Model.FilterCriteria;
import com.example.Service.Product.ProductService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@WebServlet("/product-filter")
public class ProductFilter extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /**
         * Extract filter criteria from request use for filter
         */
        FilterCriteria criteria = extractFilterCriteria(req);
        System.out.println(criteria);

        req.setAttribute("criteria", criteria);
        req.getRequestDispatcher("/product-list").forward(req, resp);
    }

    /**
     * Extracts checkbox filter parameters (OS, RAM, storage, charge) from request.
     *
     * @param request - HttpServletRequest
     * @return FilterCriteria
     */
    private FilterCriteria extractFilterCriteria(HttpServletRequest request) {
        return FilterCriteria.builder()
                .osList(safeParams(request, "os"))
                .ramList(safeParams(request, "ram"))
                .storageList(safeParams(request, "storage"))
                .chargeList(safeParams(request, "charge"))
                .build();
    }

    /**
     * Get parameters from request or return empty array if not found
     *
     * @param request - HttpServletRequest
     * @param name    - name of parameter
     * @return String[]
     */
    private String[] safeParams(HttpServletRequest request, String name) {
        return Optional.ofNullable(request.getParameterValues(name))
                .orElseGet(() -> new String[0]);
    }
}
