package com.microtech.smartshop.entity ;


import com.microtech.smartshop.enums.PaymentStatus;
import com.microtech.smartshop.enums.PaymentType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order ;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount ;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", nullable = false)
    private PaymentType paymentType ;

    @CreatedDate
    @Column(name = "payment_date", updatable = false, nullable = false)
    private LocalDateTime paymentDate ;

    @Column(name = "clearance_date")
    private LocalDateTime clearanceDate ;

    @Column(length = 20)
    private String reference ;

    @Column(length = 20)
    private String bank ;

    @Column(name = "due_date")
    private LocalDateTime dueDate ;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status ;

    /**
     * Set payment status cleared
     */
    public void clear(){
        this.status = PaymentStatus.CLEARED ;
        this.clearanceDate = LocalDateTime.now();
    }

    /**
     * Set payment status rejected
     */
    public void rejected(){
        this.status = PaymentStatus.REJECTED ;
    }

    /**
     * Initialize payment status
     */
    public void initializeStatus() {
        if (this.paymentType == PaymentType.CASH) {
            this.status = PaymentStatus.CLEARED;
            this.clearanceDate = LocalDateTime.now();
        } else {
            this.status = PaymentStatus.PENDING;
        }
    }
}