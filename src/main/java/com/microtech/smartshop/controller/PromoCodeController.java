package com.microtech.smartshop.controller;

import com.microtech.smartshop.dto.request.CreatePromoCodeRequest;
import com.microtech.smartshop.dto.response.PromoCodeResponse;
import com.microtech.smartshop.entity.PromoCode;
import com.microtech.smartshop.mapper.PromoCodeMapper;
import com.microtech.smartshop.service.PromoCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/promo-codes")
@RequiredArgsConstructor
@Tag(name = "Promo Codes", description = "Promo code management endpoints")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;
    private final PromoCodeMapper promoCodeMapper;

    /**
     * POST /api/promo-codes
     */
    @PostMapping
    @Operation(summary = "Create promo code", description = "Create a new promotional code")
    public ResponseEntity<PromoCodeResponse> create(@Valid @RequestBody CreatePromoCodeRequest request) {
        log.info("Creating new promo code: {}", request.getCode());

        PromoCode promoCode = promoCodeMapper.toEntity(request);
        PromoCode savedPromoCode = promoCodeService.create(promoCode);
        PromoCodeResponse response = promoCodeMapper.toResponse(savedPromoCode);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/promo-codes/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get promo code by ID", description = "Get promo code details by ID")
    public ResponseEntity<PromoCodeResponse> getById(@PathVariable Long id) {
        log.info("Fetching promo code: {}", id);

        PromoCode promoCode = promoCodeService.findById(id)
                .orElseThrow(() -> new RuntimeException("Promo code not found"));

        PromoCodeResponse response = promoCodeMapper.toResponse(promoCode);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/promo-codes/code/{code}
     */
    @GetMapping("/code/{code}")
    @Operation(summary = "Get promo code by code", description = "Get promo code details by code string")
    public ResponseEntity<PromoCodeResponse> getByCode(@PathVariable String code) {
        log.info("Fetching promo code: {}", code);

        PromoCode promoCode = promoCodeService.findByCode(code);
        PromoCodeResponse response = promoCodeMapper.toResponse(promoCode);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/promo-codes
     */
    @GetMapping
    @Operation(summary = "Get all promo codes", description = "Get all promo codes with pagination")
    public ResponseEntity<Page<PromoCodeResponse>> getAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Fetching all promo codes (page: {}, size: {})", pageable.getPageNumber(), pageable.getPageSize());

        Page<PromoCode> promoCodes = promoCodeService.findAll(pageable);
        Page<PromoCodeResponse> response = promoCodes.map(promoCodeMapper::toResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/promo-codes/valid
     *
     */
    @GetMapping("/valid")
    @Operation(summary = "Get valid promo codes", description = "Get all valid (unused and not expired) promo codes")
    public ResponseEntity<Page<PromoCodeResponse>> getValidPromoCodes(
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Fetching valid promo codes");

        Page<PromoCode> promoCodes = promoCodeService.findValidPromoCodes(pageable);
        Page<PromoCodeResponse> response = promoCodes.map(promoCodeMapper::toResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/promo-codes/{code}/validate
     */
    @PostMapping("/{code}/validate")
    @Operation(summary = "Validate promo code", description = "Validate if promo code is usable")
    public ResponseEntity<PromoCodeResponse> validate(@PathVariable String code) {
        log.info("Validating promo code: {}", code);

        PromoCode promoCode = promoCodeService.validatePromoCode(code);
        PromoCodeResponse response = promoCodeMapper.toResponse(promoCode);

        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/promo-codes/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete promo code", description = "Delete a promo code")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting promo code: {}", id);

        promoCodeService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}