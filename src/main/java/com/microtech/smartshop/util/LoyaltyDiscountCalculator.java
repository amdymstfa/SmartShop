package com.microtech.smartshop.util;

import com.microtech.smartshop.enums.CustomerTier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Utility for calculating loyalty discounts
 */
@Component
public class LoyaltyDiscountCalculator {

    /**
     * Calculates the discount amount based on the level and subtotal:
     * - SILVER: 5% if subtotal ≥ 500 DH
     * - GOLD: 10% if subtotal ≥ 800 DH
     * - PLATINUM: 15% if subtotal ≥ 1,200 DH
     */
    public BigDecimal calculateDiscount(CustomerTier tier, BigDecimal subtotal) {
        if (tier == null || subtotal == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal discountRate = getDiscountPercentage(tier, subtotal);

        if (discountRate.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return subtotal.multiply(discountRate)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    /**
     * Returns the applicable discount percentage
     */
    public BigDecimal getDiscountPercentage(CustomerTier tier, BigDecimal subtotal) {
        return switch (tier) {
            case SILVER -> subtotal.compareTo(BigDecimal.valueOf(500)) >= 0
                    ? BigDecimal.valueOf(5) : BigDecimal.ZERO;
            case GOLD -> subtotal.compareTo(BigDecimal.valueOf(800)) >= 0
                    ? BigDecimal.valueOf(10) : BigDecimal.ZERO;
            case PLATINUM -> subtotal.compareTo(BigDecimal.valueOf(1200)) >= 0
                    ? BigDecimal.valueOf(15) : BigDecimal.ZERO;
            default -> BigDecimal.ZERO;
        };
    }
}