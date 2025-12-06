package com.microtech.smartshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private LocalDateTime createdAt;
    private BigDecimal subtotalExcludingTax;
    private BigDecimal discountAmount;
    private BigDecimal amountAfterDiscountExclTax;
    private BigDecimal taxAmount;
    private BigDecimal totalIncludingTax;
    private BigDecimal amountRemaining;
    private String promoCode;
    private String status;
    private Boolean fullyPaid;
    private Integer itemsCount;
}