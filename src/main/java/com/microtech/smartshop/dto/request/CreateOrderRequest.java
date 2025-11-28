package com.microtech.smartshop.dto.request;

import lombok.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderRequest {
    @NotNull
    private Long customerId;

    @NotEmpty
    @Valid
    private List<OrderItemRequest> items;

    @Pattern(regexp = "PROMO-[A-Z0-9]{4}")
    private String promoCode;
}