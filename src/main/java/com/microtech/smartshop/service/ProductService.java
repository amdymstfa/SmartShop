package com.microtech.smartshop.service;

import com.microtech.smartshop.entity.Product;
import com.microtech.smartshop.service.generic.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService extends GenericService<Product, Long> {

    /**
     * Check the stock of product
     * @param id of product
     * @param quantity of product
     * @return true or false otherwise
     */
    boolean checkStock(Long id, Integer quantity);

    /**
     * Delete a product
     * @param id of product
     */
    void softDelete(Long id);

    /**
     * Restore product
     * @param id of product
     */
    void restore(Long id);

    /**
     * Search by the name of the product
     * @param name of product
     * @param pageable page of product
     * @return page
     */
    Page<Product> searchByName(String name, Pageable pageable);

    /**
     * Find all available product
     * @param pageable page of product
     * @return page
     */
    Page<Product> findAllAvailable(Pageable pageable);
}
