package com.microtech.smartshop.repository;

import com.microtech.smartshop.entity.TierHistory;
import com.microtech.smartshop.enums.CustomerTier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TierHistoryRepository extends JpaRepository<TierHistory, Long> {

    List<TierHistory> findByCustomerIdOrderByChangeDateDesc(Long customerId);

    Page<TierHistory> findByCustomerId(Long customerId, Pageable pageable);

    @Query("SELECT th FROM TierHistory th WHERE th.changeDate BETWEEN :startDate AND :endDate ORDER BY th.changeDate DESC")
    List<TierHistory> findByChangeDateBetween(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT th FROM TierHistory th WHERE th.customer.id = :customerId AND th.newTier > th.oldTier")
    List<TierHistory> findPromotionsByCustomerId(@Param("customerId") Long customerId);

    List<TierHistory> findByNewTier(CustomerTier tier);

    @Query("SELECT th FROM TierHistory th WHERE th.customer.id = :customerId ORDER BY th.changeDate DESC")
    List<TierHistory> findLatestByCustomerId(@Param("customerId") Long customerId, Pageable pageable);
}
