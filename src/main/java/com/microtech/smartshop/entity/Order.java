package com.microtech.smartshop.entity ;

import com.microtech.smartshop.enums.OrderStatus;
import com.microtech.smartshop.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "subtotal_excluding_tax", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotalExcludingTax;

    @Column(name = "discount_amount", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "amount_after_discount_excl_tax", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountAfterDiscountExclTax;

    @Column(name = "tax_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_including_tax", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalIncludingTax;

    @Column(name = "promo_code", length = 20)
    private String promoCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "amount_remaining", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountRemaining;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    /**
     * Add item in order
     * @param item, item to add
     */
    public void addItem(OrderItem item){
        this.items.add(item) ;
        item.setOrder(this);
    }

    /**
     * Calculate totals
     * @param vatRate, value to apply on an order
     */
    public void calculateTotals(BigDecimal vatRate){

        // Sum of item before vat and reduction
        this.subtotalExcludingTax = items.stream()
                .map(OrderItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // sum after reduction
        this.amountAfterDiscountExclTax = this.subtotalExcludingTax.subtract(this.discountAmount);

        // apply vat
        this.taxAmount = this.amountAfterDiscountExclTax
                .multiply(vatRate)
                .setScale(2,RoundingMode.HALF_UP);

        // add vat
        this.totalIncludingTax = this.amountAfterDiscountExclTax.add(this.taxAmount);

        if (this.amountRemaining == null){
            this.amountRemaining = this.totalIncludingTax ;
        }
    }

    /**
     * Add payment
     * @param payment of order
     */
    public void addPayment(Payment payment){
        this.payments.add(payment);
        payment.setOrder(this);
        this.amountRemaining = this.amountRemaining.subtract(payment.getAmount());
    }

    /**
     * Check payment state, fully paid or not
     */
    public boolean fullyPaid(){
        return this.amountRemaining.compareTo(BigDecimal.ZERO) == 0 ;
    }

    /**
     * Confirm payment
     */
    public boolean canBeConfirm(){
        return this.status == OrderStatus.PENDING && fullyPaid() ;
    }

    /**
     * Confirm payment
     */
    public void confirmPayment(){
        if (!canBeConfirm()){
            throw new IllegalMonitorStateException("Payment cannot be confirmed");
        }
        this.status = OrderStatus.CONFIRMED ;
    }

    /**
     * Cancel payment
     */
    public void cancel(){
        if (this.status != OrderStatus.PENDING){
            throw new IllegalStateException("Only pending status can be canceled");
        }
        this.status = OrderStatus.CANCELED ;
    }

    /**
     * Reject payment
     */
    public void reject(){
        this.status = OrderStatus.REJECTED ;
    }
}