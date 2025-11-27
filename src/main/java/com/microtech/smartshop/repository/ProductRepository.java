package com.microtech.smartshop.repository ;

import com.microtech.smartshop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Find existing product(not deleted)
     * @param pageable
     * @return Page of product
     */
    Page<Product> findByDeletedFalse(Pageable pageable);

    /**
     * Find id of existing product
     * @param id
     * @return Optional
     */
    Page<Product> findByIdAndIsDeletedFalse(Long id);

    /**
     * Search product by name
     * @param name
     * @param pageable
     * @Return Page of product
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')) AND p.isDeleted = false")
    Page<Product> findByName(@Param("name") String name, Pageable pageable);


    /**
     * Find unit price between two product
     * @param minPrice
     * @param maxPrice
     * @param pageable
     * @return
     */
    @Query("SELECT p FROM Product p WHERE p.unitPrice BETWEEN :minPrice AND :maxPrice AND p.isDeleted = false")
    Page<Product> findUnitProductBetween(@Param("minPrice") BigDecimal minPrice,
                                         @Param("maxPrice") BigDecimal maxPrice,
                                         Pageable pageable);


    /**
     *
     * @param stock of product
     * @param pageable page of product
     * @return Page
     */
    Page<Product> findByStockGreaterThanAndIsDeletedFalse(Integer stock, Pageable pageable);

    /**
     *
     * @param id
     * @return
     */
    boolean existsByIdAndIsDeletedFalse(Long id);

}