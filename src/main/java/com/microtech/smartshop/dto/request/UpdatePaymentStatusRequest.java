package com.microtech.smartshop.dto.request;

import com.microtech.smartshop.enums.PaymentStatus;
import lombok.*;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePaymentStatusRequest {
    @NotNull
    private PaymentStatus newStatus;
}