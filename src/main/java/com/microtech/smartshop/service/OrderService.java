package com.microtech.smartshop.service;

import com.microtech.smartshop.dto.request.CreateOrderRequest;
import com.microtech.smartshop.entity.Order;
import com.microtech.smartshop.enums.OrderStatus;
import com.microtech.smartshop.service.generic.GenericService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService extends GenericService<Order, Long> {

    /**
     * Create an order
     * @param request
     * @return
     */
    Order createOrder(CreateOrderRequest request);

    /**
     * Confirm an order
     * @param id
     * @return
     */
    Order confirmOrder(Long id);

    /**
     * Cancel an order
     * @param id
     * @return
     */
    Order cancelOrder(Long id);

    /**
     * Reject an order
     * @param id
     * @return
     */
    Order rejectOrder(Long id);

    /**
     * Find a customer with id
     * @param customerId
     * @param pageable
     * @return
     */
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);

    /**
     * Find an order by status
     * @param status
     * @param pageable
     * @return
     */
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);

    /**
     * Find by id with details 
     * @param id
     * @return
     */
    Order findByIdWithDetails(Long id);
}