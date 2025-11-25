package com.microtech.smartshop.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String nom;
    private String description;
    private BigDecimal priceOfProduct;
    private Integer stock;
    private Boolean available; 
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}