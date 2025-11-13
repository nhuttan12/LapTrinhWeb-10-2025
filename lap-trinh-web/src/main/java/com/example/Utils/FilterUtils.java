package com.example.Utils;

import com.example.Model.FilterCriteria;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Optional;

public class FilterUtils {
    /**
     * Extracts checkbox filter parameters (OS, RAM, storage, charge, min price. max price) from request.
     *
     * @param request - HttpServletRequest
     * @return FilterCriteria
     */
    public static FilterCriteria extractFilterCriteria(HttpServletRequest request) {
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
    private static String[] safeParams(HttpServletRequest request, String name) {
        return Optional.ofNullable(request.getParameterValues(name))
                .orElseGet(() -> new String[0]);
    }

    private static int[] toIntArray(String[] values) {
        return Arrays.stream(values)
                .filter(s -> s != null && !s.isEmpty())
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    public static boolean isCriteriaEmpty(FilterCriteria c) {
        if (c == null) return true;

        boolean hasOs = c.getOsList() != null && c.getOsList().length > 0;
        boolean hasRam = c.getRamList() != null && c.getRamList().length > 0;
        boolean hasStorage = c.getStorageList() != null && c.getStorageList().length > 0;
        boolean hasCharge = c.getChargeList() != null && c.getChargeList().length > 0;

        boolean hasPriceRange = c.getMinPrice() > 0 && c.getMaxPrice() > 0;

        return !(hasOs || hasRam || hasStorage || hasCharge || hasPriceRange);
    }
}
