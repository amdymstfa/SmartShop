package com.microtech.smartshop.util;

import com.microtech.smartshop.enums.CustomerTier;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Utility to calculate a customer's loyalty level
 */
@Component
public class CustomerTierCalculator {

    /**
     * Calculates loyalty level according to business rules:
     * - BASIC: default (0 orders)
     * - SILVER: 3 orders OR 1,000 DH accumulated
     * - GOLD: 10 orders OR 5,000 DH accumulated
     * - PLATINUM: 20 orders OR 15,000 DH accumulated
     */
    public CustomerTier calculateTier(Integer totalOrders, BigDecimal totalSpent) {
        if (totalOrders == null) totalOrders = 0;
        if (totalSpent == null) totalSpent = BigDecimal.ZERO;

        // Check PLATINUM
        if (totalOrders >= 20 || totalSpent.compareTo(BigDecimal.valueOf(15000)) >= 0) {
            return CustomerTier.PLATINUM;
        }

        // Check GOLD
        if (totalOrders >= 10 || totalSpent.compareTo(BigDecimal.valueOf(5000)) >= 0) {
            return CustomerTier.GOLD;
        }

        // Check SILVER
        if (totalOrders >= 3 || totalSpent.compareTo(BigDecimal.valueOf(1000)) >= 0) {
            return CustomerTier.SILVER;
        }

        //Default BASIC
        return CustomerTier.BASIC;
    }
}