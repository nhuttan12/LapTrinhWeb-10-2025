package com.example.DTO.Brands;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetBrandResponseDTO {
    private int id;
    private String name;
    private int productCount;
}
