package com.microtech.smartshop.dto.response;

import com.microtech.smartshop.enums.CustomerTier;
import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TierHistoryResponse {
    private Long id;
    private Long customerId;
    private String customerName;
    private CustomerTier oldTier;
    private CustomerTier newTier;
    private LocalDateTime changeDate;
    private String reason;
    private Boolean promotion;
}