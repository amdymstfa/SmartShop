package com.microtech.smartshop.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;

import lombok.Builder;

import lombok.Data;

import lombok.NoArgsConstructor;



import java.math.BigDecimal;

/**
 * DTO for product update
 * All fields are optional (only the provided fields will be updated)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductRequest {

    private String name;

    private String description;

    @Positive(message = "The unit price must be positive")

    private BigDecimal unitPrice;

    @PositiveOrZero(message = "The stock cannot be negative")
    private Integer stock;

}