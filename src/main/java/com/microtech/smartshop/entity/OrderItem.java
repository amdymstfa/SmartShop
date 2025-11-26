package com.microtech.smartshop.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.Id;


import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Entity representing a line item in an order
 * Relationship: Order -> OrderItem -> Product
 */
@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Order is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @ToString.Exclude
    private Order order;

    @NotNull(message = "Product is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @ToString.Exclude
    private Product product;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be positive")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "Unit price is required")
    @Positive(message = "Unit price must be positive")
    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @NotNull
    @PositiveOrZero
    @Column(name = "sub_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal subTotal;

    // ============================================
    // BUSINESS METHODS
    // ============================================

    /**
     * Calculate the subtotal of the line item
     * Should be called on creation or modification
     */
    public void calculateSubTotal() {
        if (this.quantity != null && this.unitPrice != null) {
            this.subTotal = this.unitPrice
                    .multiply(BigDecimal.valueOf(this.quantity))
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            this.subTotal = BigDecimal.ZERO;
        }
    }

    /**
     * Initialize unit price from the product
     * Call when creating the item
     */
    public void initializePriceFromProduct() {
        if (this.product != null) {
            this.unitPrice = this.product.getPriceOfProduct();
        }
    }

    /**
     * Factory method to create an OrderItem from a product
     */
    public static OrderItem fromProduct(Product product, Integer quantity) {
        OrderItem item = OrderItem.builder()
                .product(product)
                .quantity(quantity)
                .unitPrice(product.getPriceOfProduct())
                .build();
        item.calculateSubTotal();
        return item;
    }

    // ============================================
    // JPA LIFECYCLE CALLBACKS
    // ============================================

    @PrePersist
    @PreUpdate
    private void preSave() {
        if (unitPrice == null && product != null) {
            initializePriceFromProduct();
        }
        calculateSubTotal();
    }

    // ============================================
    // EQUALS & HASHCODE
    // ============================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderItem that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
