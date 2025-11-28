package com.microtech.smartshop.dto.response;

import com.microtech.smartshop.enums.OrderStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private LocalDateTime createdAt;
    private BigDecimal subtotalExcludingTax;
    private BigDecimal discountAmount;
    private BigDecimal amountAfterDiscountExclTax;
    private BigDecimal taxAmount;
    private BigDecimal totalIncludingTax;
    private String promoCode;
    private OrderStatus status;
    private BigDecimal amountRemaining;
    private List<OrderItemResponse> items;
    private List<PaymentResponse> payments;
}