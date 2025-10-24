package com.example.Mappers;

import com.example.DTO.User.UserChangePasswordResponseDTO;
import com.example.DTO.User.UserProfileDTO;
import com.example.Model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "fullName", target = "fullName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "userDetail.phone", target = "phone")
    @Mapping(source = "userDetail.address1", target = "address1")
    @Mapping(source = "userDetail.address2", target = "address2")
    @Mapping(source = "userDetail.address3", target = "address3")
    @Mapping(source = "userImage.image.url", target = "avatar")
    UserProfileDTO toUserProfileDTO(User user);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "username", target = "username")
    UserChangePasswordResponseDTO toUserChangePasswordResponseDto(User user);
}
