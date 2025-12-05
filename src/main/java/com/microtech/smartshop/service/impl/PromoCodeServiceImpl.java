package com.microtech.smartshop.service.impl;

import com.microtech.smartshop.entity.PromoCode;
import com.microtech.smartshop.exception.BusinessRuleException;
import com.microtech.smartshop.exception.ResourceNotFoundException;
import com.microtech.smartshop.repository.PromoCodeRepository;
import com.microtech.smartshop.service.PromoCodeService;
import com.microtech.smartshop.service.generic.GenericServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Service
@Transactional
public class PromoCodeServiceImpl
        extends GenericServiceImpl<PromoCode, Long, PromoCodeRepository>
        implements PromoCodeService {

    public PromoCodeServiceImpl(PromoCodeRepository repository) {
        super(repository);
    }

    @Override
    protected String getEntityName() {
        return "PromoCode";
    }

    @Override
    @Transactional(readOnly = true)
    public PromoCode validatePromoCode(String code) {
        PromoCode promoCode = repository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Promo code not found: " + code));

        if (!promoCode.isValid()) {
            throw new BusinessRuleException("Promo code is not valid (used or expired): " + code);
        }

        return promoCode;
    }

    @Override
    public void markAsUsed(String code) {
        PromoCode promoCode = findByCode(code);
        promoCode.markAsUsed();
        repository.save(promoCode);
        log.info("Promo code {} marked as used", code);
    }

    @Override
    @Transactional(readOnly = true)
    public PromoCode findByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Promo code not found: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PromoCode> findValidPromoCodes(Pageable pageable) {
        return repository.findValidPromoCodes(LocalDate.now(), pageable);
    }
}

