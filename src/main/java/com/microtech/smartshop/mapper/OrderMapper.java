package com.microtech.smartshop.mapper;

import com.microtech.smartshop.dto.response.OrderDetailResponse;
import com.microtech.smartshop.dto.response.OrderResponse;
import com.microtech.smartshop.entity.Order;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {OrderItemMapper.class, PaymentMapper.class}
)
public interface OrderMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(target = "fullyPaid", expression = "java(order.isFullyPaid())")
    OrderResponse toResponse(Order order);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "payments", target = "payments")
    OrderDetailResponse toDetailResponse(Order order);
}