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
}
