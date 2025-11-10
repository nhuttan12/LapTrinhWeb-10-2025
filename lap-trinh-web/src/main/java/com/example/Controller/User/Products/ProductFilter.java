package com.example.Controller.User.Products;

import com.example.Model.FilterCriteria;

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
        FilterCriteria criteria = extractFilterCriteria(req);
        System.out.println(criteria);

        req.setAttribute("criteria", criteria);
        req.getRequestDispatcher("/product-list").forward(req, resp);
    }

    /**
     * Extracts checkbox filter parameters (OS, RAM, storage, charge, min price. max price) from request.
     *
     * @param request - HttpServletRequest
     * @return FilterCriteria
     */
    private FilterCriteria extractFilterCriteria(HttpServletRequest request) {
        String priceRange = Optional.ofNullable(request.getParameter("priceRange"))
                .orElse("0 - 0");

        String priceAfterFormat = priceRange.replaceAll("[^0-9\\-]", "");

        String[] priceRangeSplit = priceAfterFormat.split("-");
        int minPrice = Integer.parseInt(Optional.ofNullable(priceRangeSplit[0]).orElse("-1"));
        int maxPrice = Integer.parseInt(Optional.ofNullable(priceRangeSplit[1]).orElse("-1"));

        return FilterCriteria.builder()
                .osList(safeParams(request, "os"))
                .ramList(toIntArray(safeParams(request, "ram")))
                .storageList(toIntArray(safeParams(request, "storage")))
                .chargeList(toIntArray(safeParams(request, "charge")))
                .minPrice(minPrice)
                .maxPrice(maxPrice)
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

    private int[] toIntArray(String[] values) {
        return Arrays.stream(values)
                .filter(s -> s != null && !s.isEmpty())
                .mapToInt(Integer::parseInt)
                .toArray();
    }
}
