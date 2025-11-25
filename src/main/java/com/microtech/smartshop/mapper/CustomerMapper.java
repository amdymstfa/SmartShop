package com.microtech.smartshop.mapper ;


import com.microtech.smartshop.dto.request.CustomerRequest;
import com.microtech.smartshop.dto.response.CustomerResponse;
import com.microtech.smartshop.entity.Customer;
import org.mapstruct.Mapping;

public interface CustomerMapper {

    // Customer -> dto
    CustomerResponse toResponseDTO(Customer customer);

    // dto -> to entity
    @Mapping(target = "totalSpent", ignore = true)
    @Mapping(target = "firstOrderDate", ignore = true)
    @Mapping(target = "id", ignore = true)
    Customer toEntity(CustomerRequest customerRequest);
}