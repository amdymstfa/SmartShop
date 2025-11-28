package com.microtech.smartshop.mapper;

import com.microtech.smartshop.dto.request.CreatePaymentRequest;
import com.microtech.smartshop.dto.response.PaymentResponse;
import com.microtech.smartshop.entity.Payment;
import org.mapstruct.*;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "paymentNumber", ignore = true)
    @Mapping(target = "paymentDate", ignore = true)
    @Mapping(target = "clearanceDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    Payment toEntity(CreatePaymentRequest request);

    @Mapping(source = "order.id", target = "orderId")
    PaymentResponse toResponse(Payment payment);

    List<PaymentResponse> toResponseList(List<Payment> payments);
}