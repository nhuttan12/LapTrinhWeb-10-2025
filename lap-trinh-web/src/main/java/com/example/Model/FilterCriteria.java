package com.example.Model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class FilterCriteria {
    private String[] osList;
    private String[] ramList;
    private String[] storageList;
    private String[] chargeList;
}
