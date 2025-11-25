package com.microtech.smartshop.mapper;

import com.microtech.smartshop.dto.request.UserRequest;
import com.microtech.smartshop.dto.response.UserResponse;
import com.microtech.smartshop.entity.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Convert an entity to dto
    UserResponse toResponseDTO(User user);

    // Convert an DTO to entity
    User toEntity(UserRequest userRequest) ;
}
