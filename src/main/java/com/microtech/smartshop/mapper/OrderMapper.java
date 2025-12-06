package com.microtech.smartshop.mapper;

import com.microtech.smartshop.dto.response.OrderDetailResponse;
import com.microtech.smartshop.dto.response.OrderResponse;
import com.microtech.smartshop.dto.response.OrderSummaryResponse;
import com.microtech.smartshop.entity.Order;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {OrderItemMapper.class, PaymentMapper.class}
)
public interface OrderMapper {


    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.nom", target = "customerName")
    @Mapping(target = "fullyPaid", expression = "java(order.isFullyPaid())")
    @Mapping(target = "itemsCount", expression = "java(order.getItems().size())")
    OrderResponse toResponse(Order order);


    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.nom", target = "customerName")
    @Mapping(source = "customer.email", target = "customerEmail")
    @Mapping(target = "fullyPaid", expression = "java(order.isFullyPaid())")
    @Mapping(target = "canBeConfirmed", expression = "java(order.canBeConfirmed())")
    @Mapping(target = "canBeCanceled", expression = "java(order.canBeCanceled())")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "payments", target = "payments")
    OrderDetailResponse toDetailResponse(Order order);


    @Mapping(source = "customer.nom", target = "customerName")
    @Mapping(target = "itemsCount", expression = "java(order.getItems().size())")
    OrderSummaryResponse toSummaryResponse(Order order);


    List<OrderSummaryResponse> toSummaryResponseList(List<Order> orders);
}