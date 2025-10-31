package com.example.DTO.Common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagingMetaData {
    private int currentPage;
    private int pageSize;
    private int totalItems;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;

    private List<String> sortBy;
    private List<SortDirection> sortDirections;

    public String buildOrderByClause(String tableAlias) {
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
