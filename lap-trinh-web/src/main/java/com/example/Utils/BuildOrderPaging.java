package com.example.Utils;

import com.example.DTO.Common.SortDirection;

import java.util.ArrayList;
import java.util.List;

public class BuildOrderPaging {
    public String buildOrderByClause(String tableAlias, List<String> sortBy, List<SortDirection> sortDirections) {
        if (sortBy == null || sortBy.isEmpty()) {
            return "ORDER BY " + tableAlias + ".id ASC";
        }

        String alias = (tableAlias == null || tableAlias.isBlank()) ? "" : tableAlias + ".";

        List<SortDirection> effectiveDirections = new ArrayList<>();

        for (int i = 0; i < sortBy.size(); i++) {
            if (sortDirections != null && i < sortDirections.size() && sortDirections.get(i) != null) {
                effectiveDirections.add(sortDirections.get(i));
            } else {
                effectiveDirections.add(SortDirection.ASC);
            }
        }

        String clause = "";
        for (int i = 0; i < sortBy.size(); i++) {
            if (i > 0) clause += ", ";
            clause += alias + sortBy.get(i) + " " + effectiveDirections.get(i).name();
        }

        return "ORDER BY " + clause;
    }
}
