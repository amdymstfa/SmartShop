package com.microtech.smartshop.service;

import com.microtech.smartshop.entity.PromoCode;

import com.microtech.smartshop.service.generic.GenericService;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface PromoCodeService extends GenericService<PromoCode, Long> {

    /**
     * Validates a promo code (exists, unused, not expired)

     */
    PromoCode validatePromoCode(String code);

    /**

     * Marks a promo code as used
     */
    void markAsUsed(String code);

    /**

     * Finds a promo code by its code
     */
    PromoCode findByCode(String code);

    /**

     * Finds all valid promo codes
     */
    Page<PromoCode> findValidPromoCodes(Pageable pageable);
}