package com.example.DTO.Banners;

import com.example.Model.ImageStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetBannerImagesPagingResponseDTO {
    private int id;
    private String url;
    private ImageStatus status;
    private String createdAt;
}
