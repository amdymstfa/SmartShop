package com.microtech.smartshop.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PromoCodeResponse {
    private Long id;
    private String code;
    private BigDecimal discountPercentage;
    private LocalDate expirationDate;
    private Boolean used;
    private Boolean valid;
    private LocalDateTime createdAt;
}