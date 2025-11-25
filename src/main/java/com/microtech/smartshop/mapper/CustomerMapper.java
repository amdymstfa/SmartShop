package com.microtech.smartshop.mapper ;


import com.microtech.smartshop.dto.request.CustomerRequest;
import com.microtech.smartshop.dto.response.CustomerResponse;
import com.microtech.smartshop.entity.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",builder = @org.mapstruct.Builder(disableBuilder = true))
public interface CustomerMapper {

    // Customer -> dto
    CustomerResponse toResponseDTO(Customer customer);

    // dto -> to entity
    // DTO -> ENTITY
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "totalOrders", constant = "0")
    @Mapping(target = "totalSpent", expression = "java(java.math.BigDecimal.ZERO)")
    @Mapping(target = "firstOrderDate", ignore = true)
    @Mapping(target = "lastOrderDate", ignore = true)
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "tier", constant = "BASIC")
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "userName", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "address", ignore = true)
    @Mapping(target = "phone", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Customer toEntity(CustomerRequest customerRequest);
}