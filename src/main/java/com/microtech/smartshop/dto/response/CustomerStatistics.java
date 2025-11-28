package com.microtech.smartshop.dto.response;

import com.microtech.smartshop.enums.CustomerTier;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerStatistics {
    private Integer totalOrders;
    private BigDecimal totalSpent;
    private CustomerTier currentTier;
    private LocalDateTime firstOrderDate;
    private LocalDateTime lastOrderDate;
}