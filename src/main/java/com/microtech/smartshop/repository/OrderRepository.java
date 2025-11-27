package com.microtech.smartshop.repository ;

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
public interface OrderRepository extends JpaRepository<String, Long> {

    /**
     * Find id with item
     *
     * @param id of item
     * @return id of item or null
     */
    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.items WHERE o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);

    /**
     *
     * @param id of item
     * @return Optional
     */
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items LEFT JOIN FETCH o.payments WHERE o.id = :id")
    Optional<Order> findByIdWithItemsAndPayment(Long id);

    /**
     * Find order with customer id
     *
     * @param customerId id of customer
     * @param pageable   page of customer id
     * @return Page
     */
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);

    /**
     * Find status of order
     *
     * @param status   order
     * @param pageable page of status
     * @return Page
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    /**
     * Find an order in a specific period
     *
     * @param startDate beginning
     * @param endDate   ending
     * @param pageable  oder
     * @return Page
     */
    @Query("SELECT o FROM Order o WHERE o.createdAt BETWEEN :startDate AND :endDate ORDER BY o.createdAt DESC")
    Page<Order> findCreateAtBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("enDate") LocalDateTime endDate,
            Pageable pageable
    );

    /**
     * @param customerId
     * @param status
     * @return
     */
    long countCustomerIdAndSatus(Long customerId, OrderStatus status);

    @Query("SELECT COALESCE(SUM(o.totalIncludingTax), 0) FROM Order o WHERE o.customer.id = :customerId AND o.status = 'CONFIRMED'")
    BigDecimal sumTotalByCustomerIdAndStatusConfirmed(
            @Param("customerId") Long customerId,
            @Param("status") OrderStatus status);

    /**
     *
     * @param promoCode
     * @param pageable
     * @return
     */
    Page<Order> findByPromoCode(String promoCode, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.amountRemaining = 0")
    List<Order> findPendingFullyPaidOrders();

}