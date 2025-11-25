package com.microtech.smartshop.entity ;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false, length = 200)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "Price of product is required")
    @Positive(message = "Price product must be positive")
    @Column(nullable = false, name = "price_of_product", precision = 12, scale = 2)
    private BigDecimal priceOfProduct;

    @NotNull(message = "Stock of product is required")
    @PositiveOrZero(message = "Stock must be positive or zero")
    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    @Column(nullable = false)
    @Builder.Default
    private boolean deleted = false;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    public boolean isAvailable() {
        return !this.deleted && this.stock > 0;
    }

    public boolean hasStock(Integer quantity) {
        return !this.deleted && this.stock >= quantity;
    }

    public void decreaseStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (this.stock < quantity) {
            throw new IllegalStateException(
                    String.format(
                            "Insufficient stock for '%s'. Available: %d, Requested: %d",
                            this.name, this.stock, quantity
                    )
            );
        }
        this.stock -= quantity;
    }

    public void incrementStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        this.stock += quantity;
    }

    public void softDelete() {
        this.deleted = true;
    }

    public void restore() {
        this.deleted = false;
    }
}
