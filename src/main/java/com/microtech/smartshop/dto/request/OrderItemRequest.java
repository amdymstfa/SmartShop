package com.microtech.smartshop.dto.request;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {
    @NotNull
    private Long productId;

    @NotNull
    @Positive
    private Integer quantity;
}