package com.microtech.smartshop.entity ;

import com.microtech.smartshop.enums.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "orders")
@EntityListeners(EntityListeners.class)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @NotNull(message = "Customer is required")
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer ;

    @CreatedDate
    @Column(name = "creation_date", nullable = false, updatable = false)
    private LocalDateTime creationDate ;

    @NotNull
    @PositiveOrZero
    @Column(name = "subtotal", nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal ;

    @NotNull
    @PositiveOrZero
    @Column(name = "discountAmount", nullable = false, precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @NotNull
    @PositiveOrZero
    @Column(name = "amountExcludingTax", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountExcludingTax ;

    @NotNull
    @PositiveOrZero
    @Column(name = "taxAmount", nullable = false, precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @NotNull
    @PositiveOrZero
    @Column(name = "totalIncludingTax", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalIncludingTax ;

    @NotNull
    @PositiveOrZero
    @Column(name = "amountRemaining", nullable = false, precision = 12, scale = 2)
    private BigDecimal amountRemaining ;

    @Column(name = "promoCode", length = 20)
    private String promoCode ;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING ;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Payment> payments = new ArrayList<>();

    /**
     * Add an item to the order
     */
    public void addItem(OrderItem orderItem){
        items.add(orderItem);
        orderItem.setOrder(this);
    }

    /**
     * Delete an item to the order
     */
    public void deleteItem(OrderItem orderItem){
        items.remove(orderItem);
        orderItem.setOrder(null);
    }

    /**
     * Calculates all order amounts.
     * Must be run after adding/modifying items.
     * @param vatRate VAT rate
     */
    public void calculateTotals(BigDecimal vatRate){

    }
}