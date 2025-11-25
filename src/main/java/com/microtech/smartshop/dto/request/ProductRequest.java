package com.microtech.smartshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    @NotBlank(message = "The product name is required")
    private String nom;

    private String description;

    @NotNull(message = "The unit price is mandatory")
    @Positive(message = "The unit price must be positive")
    private BigDecimal priceOfProduct;

    @NotNull(message = "Stock is required")
    @PositiveOrZero(message = "The stock cannot be negative")
    private Integer stock;
}