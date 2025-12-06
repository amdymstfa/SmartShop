package com.microtech.smartshop.mapper;

import com.microtech.smartshop.dto.response.OrderResponse;
import com.microtech.smartshop.dto.response.OrderDetailResponse;
import com.microtech.smartshop.dto.response.OrderSummaryResponse;
import com.microtech.smartshop.entity.Order;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, PaymentMapper.class}, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(target = "fullyPaid", expression = "java(order.getAmountRemaining().compareTo(java.math.BigDecimal.ZERO) == 0)")
    @Mapping(target = "itemsCount", expression = "java(order.getItems() != null ? order.getItems().size() : 0)")
    OrderResponse toOrderResponse(Order order);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(source = "customer.email", target = "customerEmail")
    @Mapping(source = "items", target = "items")
    @Mapping(source = "payments", target = "payments")
    @Mapping(target = "fullyPaid", expression = "java(order.getAmountRemaining().compareTo(java.math.BigDecimal.ZERO) == 0)")
    @Mapping(target = "canBeConfirmed", expression = "java(order.getStatus() == com.microtech.smartshop.enums.OrderStatus.PENDING && order.getAmountRemaining().compareTo(java.math.BigDecimal.ZERO) == 0)")
    @Mapping(target = "canBeCanceled", expression = "java(order.getStatus() == com.microtech.smartshop.enums.OrderStatus.PENDING || order.getStatus() == com.microtech.smartshop.enums.OrderStatus.CONFIRMED)")
    OrderDetailResponse toOrderDetailResponse(Order order);

    @Mapping(source = "customer.name", target = "customerName")
    @Mapping(target = "itemsCount", expression = "java(order.getItems() != null ? order.getItems().size() : 0)")
    OrderSummaryResponse toOrderSummaryResponse(Order order);

    default OrderResponse toResponse(Order order) {
        return toOrderResponse(order);
    }

    default OrderDetailResponse toDetailResponse(Order order) {
        return toOrderDetailResponse(order);
    }

    default OrderSummaryResponse toSummaryResponse(Order order) {
        return toOrderSummaryResponse(order);
    }
}
