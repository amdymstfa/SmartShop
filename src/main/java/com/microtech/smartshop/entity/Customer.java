package com.microtech.smartshop.entity;

import com.microtech.smartshop.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Customer extends User {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CustomerTier tier = CustomerTier.BASIC;

    @Column(name = "total_orders", nullable = false)
    @Builder.Default
    private Integer totalsOrders = 0;

    @Column(name = "total_spent", precision = 12, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal totalSpent = BigDecimal.ZERO;

    @Column(name = "first_order_date")
    private LocalDateTime firstOrderDate;

    @Column(name = "last_order_date")
    private LocalDateTime lastOrderDate;

    @Column(nullable = false)
    @Builder.Default
    private Boolean deleted = false;


    /**
     * Update statics after an order
     * @param orderAmount order
     */
    public void updateStatics(BigDecimal orderAmount){
        this.totalsOrders++;
        this.totalSpent = this.totalSpent.add(orderAmount);
        this.lastOrderDate = LocalDateTime.now();

        if (this.firstOrderDate == null){
            this.firstOrderDate = LocalDateTime.now();
        }
    }

    /**
     * Reset statics fot test
     */
    public void resetStatics(){
        this.totalsOrders = 0 ;
        this.totalSpent = BigDecimal.ZERO ;
        this.firstOrderDate = null ;
        this.lastOrderDate = null ;
        this.tier = CustomerTier.BASIC ;
    }

    /**
     * Mark client as deleted
     */
    public boolean softDelete(){
        return this.deleted = true ;
    }

    /**
     * Restore a deleted customer
     */
    public boolean restore(){
        return this.deleted = false ;
    }

    /**
     * Check customer status
     */
    public boolean isActive() {
        return !this.deleted && this.isStatus();
    }
}
