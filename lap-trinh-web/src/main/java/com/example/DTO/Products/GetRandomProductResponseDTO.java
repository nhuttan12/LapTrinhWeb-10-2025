package com.example.DTO.Products;

import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetRandomProductResponseDTO {
    int id;
    String name;
    String imageUrl;
    double price;
    double discount;
}
