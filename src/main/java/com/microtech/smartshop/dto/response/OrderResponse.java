package com.microtech.smartshop.dto.response;

import com.microtech.smartshop.enums.OrderStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private LocalDateTime createdAt;
    private BigDecimal subtotalExcludingTax;
    private BigDecimal discountAmount;
    private BigDecimal totalIncludingTax;
    private OrderStatus status;
    private BigDecimal amountRemaining;
    private Boolean fullyPaid;
}