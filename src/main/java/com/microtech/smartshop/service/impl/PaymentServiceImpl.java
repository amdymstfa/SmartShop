package com.microtech.smartshop.service.impl;

import com.microtech.smartshop.dto.request.CreatePaymentRequest;
import com.microtech.smartshop.entity.Order;
import com.microtech.smartshop.entity.Payment;
import com.microtech.smartshop.enums.PaymentStatus;
import com.microtech.smartshop.enums.PaymentType;
import com.microtech.smartshop.exception.BusinessRuleException;
import com.microtech.smartshop.exception.CashLimitExceededException;
import com.microtech.smartshop.repository.OrderRepository;
import com.microtech.smartshop.repository.PaymentRepository;
import com.microtech.smartshop.service.PaymentService;
import com.microtech.smartshop.service.generic.GenericServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@Transactional
public class PaymentServiceImpl
        extends GenericServiceImpl<Payment, Long, PaymentRepository>
        implements PaymentService {

    private final OrderRepository orderRepository;

    @Value("${app.payment.cash-limit:20000.00}")
    private BigDecimal cashLimit;

    public PaymentServiceImpl(PaymentRepository repository, OrderRepository orderRepository) {
        super(repository);
        this.orderRepository = orderRepository;
    }

    @Override
    protected String getEntityName() {
        return "Payment";
    }

    @Override
    public Payment registerPayment(Long orderId, CreatePaymentRequest request) {
        log.info("Registering payment for order {}", orderId);

        // 1. Retrieve the order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessRuleException("Order not found"));

        if (order.isFinalStatus()) {
            throw new BusinessRuleException("Cannot add payment to a finalized order");
        }

        // 2. Validate payment amount
        if (request.getAmount().compareTo(order.getAmountRemaining()) > 0) {
            throw new BusinessRuleException(
                    String.format("Payment amount %.2f exceeds remaining amount %.2f",
                            request.getAmount(), order.getAmountRemaining()));
        }

        // 3. Validate cash limit
        if (request.getPaymentType() == PaymentType.CASH &&
                request.getAmount().compareTo(cashLimit) > 0) {
            throw new CashLimitExceededException(
                    "Cash payment cannot exceed " + cashLimit + " DH");
        }

        // 4. Calculate next payment number
        Integer paymentNumber = repository.findMaxPaymentNumberByOrderId(orderId) + 1;

        // 5. Create payment entity
        Payment payment = Payment.builder()
                .order(order)
                .paymentNumber(paymentNumber)
                .amount(request.getAmount())
                .paymentType(request.getPaymentType())
                .reference(request.getReference())
                .bank(request.getBank())
                .dueDate(request.getDueDate())
                .build();

        // 6. Initialize default status depending on the type
        payment.initializeStatus();

        // 7. Validate required fields based on payment type
        validatePaymentData(payment);

        // 8. Add the payment to the order
        order.addPayment(payment);

        // 9. Save payment and update order
        Payment savedPayment = repository.save(payment);
        orderRepository.save(order);

        log.info("Payment {} registered for order {}. Amount: {} DH, Remaining: {} DH",
                savedPayment.getId(), orderId, savedPayment.getAmount(), order.getAmountRemaining());

        return savedPayment;
    }

    @Override
    public Payment updateStatus(Long id, PaymentStatus newStatus) {
        Payment payment = findByIdOrThrow(id);

        if (payment.getStatus() == PaymentStatus.CLEARED && newStatus == PaymentStatus.REJECTED) {
            throw new BusinessRuleException("Cannot reject a cleared payment");
        }

        PaymentStatus oldStatus = payment.getStatus();

        // Handle status update
        if (newStatus == PaymentStatus.CLEARED) {
            payment.clear();
        } else if (newStatus == PaymentStatus.REJECTED) {
            payment.reject();

            // Restore amount remaining on the order
            Order order = payment.getOrder();
            order.restoreAmountRemaining(payment.getAmount());
            orderRepository.save(order);
        }

        repository.save(payment);
        log.info("Payment {} status updated: {} → {}", id, oldStatus, newStatus);

        return payment;
    }

    @Override
    public Payment clearPayment(Long id) {
        return updateStatus(id, PaymentStatus.CLEARED);
    }

    @Override
    public Payment rejectPayment(Long id) {
        return updateStatus(id, PaymentStatus.REJECTED);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Payment> findByOrderId(Long orderId) {
        return repository.findByOrderIdOrderByPaymentNumberAsc(orderId);
    }

    /**
     * Validates required fields depending on the payment type.
     */
    private void validatePaymentData(Payment payment) {
        switch (payment.getPaymentType()) {
            case CHECK:
                if (payment.getReference() == null ||
                        payment.getBank() == null ||
                        payment.getDueDate() == null) {
                    throw new BusinessRuleException(
                            "Check payments require reference, bank, and due date"
                    );
                }
                break;

            case TRANSFER:
                if (payment.getReference() == null ||
                        payment.getBank() == null) {
                    throw new BusinessRuleException(
                            "Transfer payments require reference and bank"
                    );
                }
                break;

            case CASH:
                // No extra validation needed
                break;
        }
    }
}
