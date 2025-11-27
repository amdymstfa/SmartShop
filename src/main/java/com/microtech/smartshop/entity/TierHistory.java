package com.microtech.smartshop.entity;

import com.microtech.smartshop.enums.CustomerTier;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "tier_history")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TierHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "old_tier", nullable = false)
    private CustomerTier oldTier;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_tier", nullable = false)
    private CustomerTier newTier;

    @CreatedDate
    @Column(name = "change_date", nullable = false, updatable = false)
    private LocalDateTime changeDate;

    @Column(length = 255)
    private String reason;

    /**
     * Define promo
     * @return true or false otherwise
     */
    public boolean isPromotion() {
        return this.newTier.ordinal() > this.oldTier.ordinal();
    }
}