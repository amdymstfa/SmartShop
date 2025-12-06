package com.microtech.smartshop.mapper;

import com.microtech.smartshop.dto.request.CreateCustomerRequest;
import com.microtech.smartshop.dto.request.UpdateCustomerRequest;
import com.microtech.smartshop.dto.response.CustomerResponse;
import com.microtech.smartshop.entity.Customer;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerMapper {
    Customer toEntity(CreateCustomerRequest request);

    void updateEntityFromRequest(UpdateCustomerRequest request, @MappingTarget Customer customer);

    CustomerResponse toResponse(Customer customer);
}