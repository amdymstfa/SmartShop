package com.microtech.smartshop.repository;

import com.microtech.smartshop.entity.Payment;
import com.microtech.smartshop.enums.PaymentStatus;
import com.microtech.smartshop.enums.PaymentType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrderIdOrderByPaymentNumberAsc(Long orderId);

    long countByOrderId(Long orderId);

    @Query("SELECT COALESCE(MAX(p.paymentNumber), 0) FROM Payment p WHERE p.order.id = :orderId")
    Integer findMaxPaymentNumberByOrderId(@Param("orderId") Long orderId);

    Optional<Payment> findByStatus(PaymentStatus status, Pageable pageable);

    Optional<Payment> findByPaymentType(PaymentType paymentType, Pageable pageable);

    @Query("SELECT COALESCE(SUM(p.amount), 0) FROM Payment p WHERE p.order.id = :orderId AND p.status = 'CLEARED'")
    BigDecimal sumClearedPaymentsByOrderId(@Param("orderId") Long orderId);

    List<Payment> findByStatusAndOrderId(PaymentStatus status, Long orderId);

    boolean existsByOrderId(Long orderId);
}