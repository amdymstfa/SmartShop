package com.microtech.smartshop.dto.request;

import lombok.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {
    private String name;
    private String description;

    @Positive
    private BigDecimal unitPrice;

    @PositiveOrZero
    private Integer stock;
}