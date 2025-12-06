package com.microtech.smartshop.repository;

import com.microtech.smartshop.entity.PromoCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromoCodeRepository extends JpaRepository<PromoCode, Long> {

    Optional<PromoCode> findByCode(String code);

    boolean existsByCode(String code);

    Page<PromoCode> findByUsedFalse(Pageable pageable);

    @Query("SELECT p FROM PromoCode p WHERE p.used = false AND (p.expirationDate IS NULL OR p.expirationDate >= :currentDate)")
    Page<PromoCode> findValidPromoCodes(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    @Query("SELECT p FROM PromoCode p WHERE p.code = :code AND p.used = false AND (p.expirationDate IS NULL OR p.expirationDate >= :currentDate)")
    Optional<PromoCode> findValidPromoCodeByCode(
            @Param("code") String code,
            @Param("currentDate") LocalDate currentDate
    );

    @Query("SELECT p FROM PromoCode p WHERE p.used = false AND p.expirationDate BETWEEN :startDate AND :endDate")
    List<PromoCode> findExpiringPromoCodes(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
