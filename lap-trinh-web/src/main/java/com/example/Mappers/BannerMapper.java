package com.example.Mappers;

import com.example.DTO.Banners.GetBannerImagesPagingResponseDTO;
import com.example.Model.Banner;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper()
public interface BannerMapper {
    BannerMapper INSTANCE = Mappers.getMapper(BannerMapper.class);

    @Named("timestampToString")
    default String timestampToString(Timestamp timestamp) {
        if (timestamp == null) {
            return null;
        }
        return timestamp.toLocalDateTime()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    @Mapping(source = "id", target = "id")
    @Mapping(source = "image.url", target = "url")
    @Mapping(source = "image.status", target = "status")
    @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "timestampToString")
    GetBannerImagesPagingResponseDTO toGetBannerImagesPagingResponseDTO(Banner banner);

    List<GetBannerImagesPagingResponseDTO> toGetBannerImagesPagingResponseDTOList(List<Banner> banners);
}
