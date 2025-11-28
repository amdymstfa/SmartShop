package com.microtech.smartshop.dto.request;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProductRequest {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    @Positive
    private BigDecimal unitPrice;

    @NotNull
    @PositiveOrZero
    private Integer stock;
}