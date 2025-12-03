package com.microtech.smartshop.service;

import com.microtech.smartshop.entity.Customer;
import com.microtech.smartshop.entity.TierHistory;
import com.microtech.smartshop.enums.CustomerTier;
import com.microtech.smartshop.service.generic.GenericService;

import java.util.List;

public interface TierHistoryService extends GenericService<TierHistory, Long> {

    /**
     * Creates a level change history
     */
    TierHistory createTierChange(Customer customer, CustomerTier oldTier, CustomerTier newTier, String reason);

    /**
     * Retrieves a customer's history
     */
    List<TierHistory> findByCustomerId(Long customerId);

    /**

     * Retrieves a customer's promotions

     */
    List<TierHistory> findPromotionsByCustomerId(Long customerId);

}