package com.example.Mappers;

import com.example.DTO.Banners.GetBannerImagesPagingResponseDTO;
import com.example.Model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper()
public interface ImageMapper {
    ImageMapper INSTANCE = Mappers.getMapper(ImageMapper.class);
}
