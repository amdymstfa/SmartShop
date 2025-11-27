package com.microtech.smartshop.entity ;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promo_codes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NotNull
@AllArgsConstructor
public class PromoCode
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(nullable = false, unique = true)
    private String code ;

    @Column(name = "discount_percentage", precision = 5, scale = 2, nullable = false)
    @Builder.Default
    private BigDecimal discountPercentage = BigDecimal.valueOf(5.00) ;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    @Builder.Default
    private boolean used = false ;

    @CreatedDate
    @Column(name = "create_date", nullable = false, updatable = false)
    private LocalDateTime createdAt ;

    /**
     * Availability of usage
     */
    public boolean isValid(){
        if (this.used) return false ;
        if (this.expirationDate == null) return true ;
        return !this.expirationDate.isAfter(LocalDateTime.now());
    }

    /**
     * Mark as used
     */
    public void markAsUsed(){
        if (!isValid()){
            throw new IllegalStateException("Code promo invalid");
        }
        this.used = true ;
    }


}