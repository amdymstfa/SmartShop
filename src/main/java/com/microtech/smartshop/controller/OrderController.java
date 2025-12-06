package com.microtech.smartshop.controller;

import com.microtech.smartshop.dto.request.CreateOrderRequest;
import com.microtech.smartshop.dto.response.OrderDetailResponse;
import com.microtech.smartshop.dto.response.OrderResponse;
import com.microtech.smartshop.entity.Order;
import com.microtech.smartshop.enums.OrderStatus;
import com.microtech.smartshop.mapper.OrderMapper;
import com.microtech.smartshop.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management endpoints")
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    /**
     * POST /api/orders
     */
    @PostMapping
    @Operation(summary = "Create order", description = "Create a new order with items and calculations")
    public ResponseEntity<OrderDetailResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        log.info("Creating new order for customer: {}", request.getCustomerId());

        Order order = orderService.createOrder(request);
        OrderDetailResponse response = orderMapper.toDetailResponse(order);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * GET /api/orders/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Get order details with items and payments")
    public ResponseEntity<OrderDetailResponse> getById(@PathVariable Long id) {
        log.info("Fetching order: {}", id);

        Order order = orderService.findByIdWithDetails(id);
        OrderDetailResponse response = orderMapper.toDetailResponse(order);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/orders
     */
    @GetMapping
    @Operation(summary = "Get all orders", description = "Get all orders with pagination")
    public ResponseEntity<Page<OrderResponse>> getAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        log.info("Fetching all orders (page: {}, size: {})", pageable.getPageNumber(), pageable.getPageSize());

        Page<Order> orders = orderService.findAll(pageable);
        Page<OrderResponse> response = orders.map(orderMapper::toResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/orders/customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get customer orders", description = "Get all orders for a specific customer")
    public ResponseEntity<Page<OrderResponse>> getByCustomerId(
            @PathVariable Long customerId,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Fetching orders for customer: {}", customerId);

        Page<Order> orders = orderService.findByCustomerId(customerId, pageable);
        Page<OrderResponse> response = orders.map(orderMapper::toResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/orders/status/{status}
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get orders by status", description = "Get all orders with a specific status")
    public ResponseEntity<Page<OrderResponse>> getByStatus(
            @PathVariable OrderStatus status,
            @PageableDefault(size = 10) Pageable pageable) {
        log.info("Fetching orders with status: {}", status);

        Page<Order> orders = orderService.findByStatus(status, pageable);
        Page<OrderResponse> response = orders.map(orderMapper::toResponse);

        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/orders/{id}/confirm
     */
    @PatchMapping("/{id}/confirm")
    @Operation(summary = "Confirm order", description = "Confirm an order (requires full payment)")
    public ResponseEntity<OrderDetailResponse> confirm(@PathVariable Long id) {
        log.info("Confirming order: {}", id);

        Order order = orderService.confirmOrder(id);
        OrderDetailResponse response = orderMapper.toDetailResponse(order);

        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/orders/{id}/cancel
     */
    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancel order", description = "Cancel a PENDING order")
    public ResponseEntity<OrderDetailResponse> cancel(@PathVariable Long id) {
        log.info("Canceling order: {}", id);

        Order order = orderService.cancelOrder(id);
        OrderDetailResponse response = orderMapper.toDetailResponse(order);

        return ResponseEntity.ok(response);
    }

    /**
     * PATCH /api/orders/{id}/reject
     */
    @PatchMapping("/{id}/reject")
    @Operation(summary = "Reject order", description = "Reject an order (insufficient stock)")
    public ResponseEntity<OrderDetailResponse> reject(@PathVariable Long id) {
        log.info("Rejecting order: {}", id);

        Order order = orderService.rejectOrder(id);
        OrderDetailResponse response = orderMapper.toDetailResponse(order);

        return ResponseEntity.ok(response);
    }
}