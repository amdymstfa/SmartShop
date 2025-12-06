package com.microtech.smartshop.repository;

import com.microtech.smartshop.entity.Order;
import com.microtech.smartshop.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items LEFT JOIN FETCH o.payments WHERE o.id = :id")
    Optional<Order> findByIdWithItemsAndPayment(@Param("id") Long id);

    Page<Order> findByCustomerId(Long customerId, Pageable pageable);

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    List<Order> findCreateAtBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            Pageable pageable
    );

    long countByCustomerIdAndStatus(Long customerId, OrderStatus status);

    @Query("SELECT COALESCE(SUM(o.totalIncludingTax), 0) FROM Order o WHERE o.customer.id = :customerId AND o.status = 'CONFIRMED'")
    BigDecimal sumTotalByCustomerIdAndStatusConfirmed(@Param("customerId") Long customerId);

    Page<Order> findByPromoCode(String promoCode, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.amountRemaining = 0")
    List<Order> findPendingFullyPaidOrders();
}
