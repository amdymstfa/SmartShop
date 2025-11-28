package com.microtech.smartshop.dto.request;

import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePromoCodeRequest {
    @NotBlank
    @Pattern(regexp = "PROMO-[A-Z0-9]{4}")
    private String code;

    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("100.00")
    private BigDecimal discountPercentage;

    private LocalDate expirationDate;
}
