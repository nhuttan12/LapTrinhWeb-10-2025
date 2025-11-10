package com.example.Model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FilterCriteria {
    private String[] osList;
    private int[] ramList;
    private int[] storageList;
    private int[] chargeList;
    private int minPrice;
    private int maxPrice;
}
