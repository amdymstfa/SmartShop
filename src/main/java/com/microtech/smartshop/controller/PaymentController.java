package com.microtech.smartshop.controller;

import com.microtech.smartshop.dto.request.CreatePaymentRequest;
import com.microtech.smartshop.dto.request.UpdatePaymentStatusRequest;
import com.microtech.smartshop.dto.response.PaymentResponse;
import com.microtech.smartshop.entity.Payment;
import com.microtech.smartshop.mapper.PaymentMapper;
import com.microtech.smartshop.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment management endpoints")
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    /**
     * POST /api/payments/order/{orderId}
     */
    @PostMapping("/order/{orderId}")
    @Operation(summary = "Register payment", description = "Register a new payment for an order")
    public ResponseEntity<PaymentResponse> registerPayment(
            @PathVariable Long orderId,
            @Valid @RequestBody CreatePaymentRequest request) {
        log.info("Registering payment for order {}: {} DH", orderId, request.getAmount());

        Payment payment = paymentService.registerPayment(orderId, request);
        PaymentResponse response = paymentMapper.toResponse(payment);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/payments/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID", description = "Get payment details by ID")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {
        log.info("Fetching payment: {}", id);

        Payment payment = paymentService.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        PaymentResponse response = paymentMapper.toResponse(payment);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/payments/order/{orderId}
     */
    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get order payments", description = "Get all payments for an order")
    public ResponseEntity<List<PaymentResponse>> getByOrderId(@PathVariable Long orderId) {
        log.info("Fetching payments for order: {}", orderId);

        List<Payment> payments = paymentService.findByOrderId(orderId);
        List<PaymentResponse> response = paymentMapper.toResponseList(payments);

        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/payments/{id}/status
     */
    @PatchMapping("/{id}/status")
    @Operation(summary = "Update payment status", description = "Update payment status (PENDING, CLEARED, REJECTED)")
    public ResponseEntity<PaymentResponse> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePaymentStatusRequest request) {
        log.info("Updating payment {} status to: {}", id, request.getNewStatus());

        Payment payment = paymentService.updateStatus(id, request.getNewStatus());
        PaymentResponse response = paymentMapper.toResponse(payment);

        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/payments/{id}/clear
     */
    @PatchMapping("/{id}/clear")
    @Operation(summary = "Clear payment", description = "Mark payment as cleared")
    public ResponseEntity<PaymentResponse> clearPayment(@PathVariable Long id) {
        log.info("Clearing payment: {}", id);

        Payment payment = paymentService.clearPayment(id);
        PaymentResponse response = paymentMapper.toResponse(payment);

        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/payments/{id}/reject
     */
    @PatchMapping("/{id}/reject")
    @Operation(summary = "Reject payment", description = "Reject a payment")
    public ResponseEntity<PaymentResponse> rejectPayment(@PathVariable Long id) {
        log.info("Rejecting payment: {}", id);

        Payment payment = paymentService.rejectPayment(id);
        PaymentResponse response = paymentMapper.toResponse(payment);

        return ResponseEntity.ok(response);
    }
}



