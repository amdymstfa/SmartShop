package com.microtech.smartshop.entity;

import com.microtech.smartshop.enums.PaymentStatus;
import com.microtech.smartshop.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer paymentNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private String reference;

    private String bank;

    private LocalDate dueDate;

    private LocalDateTime clearedAt;

    private LocalDateTime rejectedAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    public void initializeStatus() {
        if (this.paymentType == PaymentType.CASH) {
            this.status = PaymentStatus.CLEARED;
            this.clearedAt = LocalDateTime.now();
        } else {
            this.status = PaymentStatus.PENDING;
        }
    }


    public void clear() {
        this.status = PaymentStatus.CLEARED;
        this.clearedAt = LocalDateTime.now();
        this.rejectedAt = null;
    }


    public void reject() {
        this.status = PaymentStatus.REJECTED;
        this.rejectedAt = LocalDateTime.now();
        this.clearedAt = null;
    }
}
