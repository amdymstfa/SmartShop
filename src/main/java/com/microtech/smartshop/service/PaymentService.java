package com.microtech.smartshop.service;

import com.microtech.smartshop.dto.request.CreatePaymentRequest;
import com.microtech.smartshop.entity.Payment;
import com.microtech.smartshop.enums.PaymentStatus;
import com.microtech.smartshop.service.generic.GenericService;

import java.util.List;

public interface PaymentService extends GenericService<Payment, Long> {

    /**
     * Register a payment
     * @param orderId payment
     * @param request for creation
     * @return payment
     */
    Payment registerPayment(Long orderId, CreatePaymentRequest request);

    /**
     * Update status of payment
     * @param id of payment
     * @param newStatus current status
     * @return payment
     */
    Payment updateStatus(Long id, PaymentStatus newStatus);

    /**
     * Find by order
     * @param orderId id of payment
     * @return list of payment
     */
    List<Payment> findByOrderId(Long orderId);

    /**
     * Payment to clear
     * @param id of payment
     * @return payment
     */
    Payment clearPayment(Long id);

    /**
     * Payment to reject
     * @param id of payment
     * @return a payment
     */
    Payment rejectPayment(Long id);
}