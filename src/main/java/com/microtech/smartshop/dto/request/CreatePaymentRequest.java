package com.microtech.smartshop.dto.request;

import com.microtech.smartshop.enums.PaymentType;
import lombok.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePaymentRequest {
    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private PaymentType paymentType;

    private String reference;
    private String bank;
    private LocalDate dueDate;
}