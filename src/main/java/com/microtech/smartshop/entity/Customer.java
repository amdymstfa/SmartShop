package com.microtech.smartshop.entity;

import com.microtech.smartshop.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends User {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CustomerTier tier = CustomerTier.BASIC;

    @Column(name = "total_orders", nullable = false)
    @Builder.Default
    private Integer totalOrders = 0;

    @Column(name = "total_spent", precision = 12, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "first_order_date")
    private LocalDateTime firstOrderDate;

    @Column(name = "last_order_date")
    private LocalDateTime lastOrderDate;

    @Column(name = "is_deleted", nullable = false)
    @Builder.Default
    private Boolean isDeleted = false;

    /**
     * Update statics
     * @param orderAmount
     */
    public void updateStatistics(BigDecimal orderAmount) {
        this.totalOrders++;
        this.totalSpent = this.totalSpent.add(orderAmount);
        this.lastOrderDate = LocalDateTime.now();
        if (this.firstOrderDate == null) {
            this.firstOrderDate = LocalDateTime.now();
        }
    }

    public void softDelete() {
        this.isDeleted = true;
    }

    public void restore() {
        this.isDeleted = false;
    }
}