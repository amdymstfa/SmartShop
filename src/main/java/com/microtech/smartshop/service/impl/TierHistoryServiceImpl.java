package com.microtech.smartshop.service.impl;

import com.microtech.smartshop.entity.Customer;
import com.microtech.smartshop.entity.TierHistory;
import com.microtech.smartshop.enums.CustomerTier;
import com.microtech.smartshop.repository.TierHistoryRepository;
import com.microtech.smartshop.service.TierHistoryService;
import com.microtech.smartshop.service.generic.GenericServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class TierHistoryServiceImpl
        extends GenericServiceImpl<TierHistory, Long, TierHistoryRepository>
        implements TierHistoryService {

    public TierHistoryServiceImpl(TierHistoryRepository repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "TierHistory";
    }

    @Override
    public TierHistory createTierChange(Customer customer, CustomerTier oldTier, CustomerTier newTier, String reason) {
        TierHistory history = TierHistory.builder()
                .customer(customer)
                .oldTier(oldTier)
                .newTier(newTier)
                .reason(reason)
                .build();

        TierHistory saved = repository.save(history);
        log.info("Tier change recorded for customer {}: {} → {}",
                customer.getId(), oldTier, newTier);

        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TierHistory> findByCustomerId(Long customerId) {
        return repository.findByCustomerIdOrderByChangeDateDesc(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TierHistory> findPromotionsByCustomerId(Long customerId) {
        return repository.findPromotionsByCustomerId(customerId);
    }
}
