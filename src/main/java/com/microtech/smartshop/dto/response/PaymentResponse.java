package com.microtech.smartshop.dto.response;

import com.microtech.smartshop.enums.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Long id;
    private Long orderId;
//    private Integer paymentNumber;
    private BigDecimal amount;
    private PaymentType paymentType;
    private LocalDateTime paymentDate;
    private LocalDateTime clearanceDate;
    private String reference;
    private String bank;
    private LocalDate dueDate;
    private PaymentStatus status;
}