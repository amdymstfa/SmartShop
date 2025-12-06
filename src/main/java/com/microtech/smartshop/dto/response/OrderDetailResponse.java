package com.microtech.smartshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private String customerEmail;
    private LocalDateTime createdAt;
    private BigDecimal subtotalExcludingTax;
    private BigDecimal discountAmount;
    private BigDecimal amountAfterDiscountExclTax;
    private BigDecimal taxAmount;
    private BigDecimal totalIncludingTax;
    private BigDecimal amountRemaining;
    private String promoCode;
    private String status;
    private List<OrderItemResponse> items;
    private List<PaymentResponse> payments;
    private Boolean fullyPaid;
    private Boolean canBeConfirmed;
    private Boolean canBeCanceled;
}