package com.microtech.smartshop.service;

import com.microtech.smartshop.dto.response.CustomerStatistics;
import com.microtech.smartshop.entity.Customer;
import com.microtech.smartshop.entity.Order;
import com.microtech.smartshop.service.generic.GenericService;

import java.util.List;

public interface CustomerService extends GenericService<Customer, Long> {

    /**
     * Retrieve statics customer
     * @param id of customer
     * @return statics customer
     */
    CustomerStatistics getCustomerStatistics(Long id);

    /**
     * Retrieve list order
     * @param id of order
     * @return list of order story
     */
    List<Order> getOrderHistory(Long id);

    /**
     * Update level of customer
     * @param id
     */
    void updateTierLevel(Long id);

    /**
     * Find customer mail
     * @param email of customer
     * @return customer
     */
    Customer findByEmail(String email);

    /**
     * delete
     * @param id
     */
    void softDelete(Long id);
}