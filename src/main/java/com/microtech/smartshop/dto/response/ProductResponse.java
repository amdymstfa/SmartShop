package com.microtech.smartshop.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal unitPrice;
    private Integer stock;
    private Boolean available;
    private LocalDateTime createdAt;
}
