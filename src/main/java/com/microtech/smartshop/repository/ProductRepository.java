package com.microtech.smartshop.repository;

import com.microtech.smartshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByIsDeletedFalse(Pageable pageable);

    Optional<Product> findByIdAndIsDeletedFalse(Long id);

    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.isDeleted = false")
    Page<Product> findByName(@Param("name") String name, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.unitPrice BETWEEN :minPrice AND :maxPrice AND p.isDeleted = false")
    Page<Product> findUnitProductBetween(@Param("minPrice") BigDecimal minPrice,
                                         @Param("maxPrice") BigDecimal maxPrice,
                                         Pageable pageable);

    Page<Product> findByStockGreaterThanAndIsDeletedFalse(Integer stock, Pageable pageable);

    boolean existsByIdAndIsDeletedFalse(Long id);

    Page<Product> searchByName(String name, Pageable pageable);
}
