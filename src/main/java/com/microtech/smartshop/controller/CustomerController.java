package com.microtech.smartshop.controller;

import com.microtech.smartshop.dto.request.CreateCustomerRequest;
import com.microtech.smartshop.dto.request.UpdateCustomerRequest;
import com.microtech.smartshop.dto.response.CustomerResponse;
import com.microtech.smartshop.dto.response.CustomerStatistics;
import com.microtech.smartshop.dto.response.OrderSummaryResponse;
import com.microtech.smartshop.entity.Customer;
import com.microtech.smartshop.mapper.CustomerMapper;
import com.microtech.smartshop.mapper.OrderMapper;
import com.microtech.smartshop.service.CustomerService;
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

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "Customer management endpoints")
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;
    private final OrderMapper orderMapper;

    @PostMapping
    @Operation(summary = "Create customer", description = "Create a new customer")
    public ResponseEntity<CustomerResponse> create(@Valid @RequestBody CreateCustomerRequest request) {
        log.info("Creating new customer: {}", request.getEmail());

        Customer customer = customerMapper.toEntity(request);
        Customer savedCustomer = customerService.create(customer);
        CustomerResponse response = customerMapper.toResponse(savedCustomer);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Get customer details by ID")
    public ResponseEntity<CustomerResponse> getById(@PathVariable Long id) {
        log.info("Fetching customer: {}", id);

        Customer customer = customerService.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        CustomerResponse response = customerMapper.toResponse(customer);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all customers", description = "Get all customers with pagination")
    public ResponseEntity<Page<CustomerResponse>> getAll(
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        log.info("Fetching all customers (page: {}, size: {})", pageable.getPageNumber(), pageable.getPageSize());

        Page<Customer> customers = customerService.findAll(pageable);
        Page<CustomerResponse> responses = customers.map(customerMapper::toResponse);

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Update customer information")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCustomerRequest request
    ) {
        log.info("Updating customer {}", id);

        Customer customer = customerService.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));


        customerMapper.updateEntityFromRequest(request, customer);
        Customer updatedCustomer = customerService.update(id, customer);
        CustomerResponse response = customerMapper.toResponse(updatedCustomer);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer", description = "Soft delete a customer")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Deleting customer: {}", id);

        customerService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/statistics")
    @Operation(summary = "Get customer statistics", description = "Get customer order statistics")
    public ResponseEntity<CustomerStatistics> getStatistics(@PathVariable Long id) {
        log.info("Fetching statistics for customer: {}", id);

        CustomerStatistics stats = customerService.getCustomerStatistics(id);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/{id}/orders")
    @Operation(summary = "Get customer orders", description = "Get customer order history")
    public ResponseEntity<List<OrderSummaryResponse>> getOrder(@PathVariable Long id) {
        log.info("Fetching orders for customer: {}", id);

        List<OrderSummaryResponse> orders = customerService.getOrderHistory(id)
                .stream()
                .map(orderMapper::toOrderSummaryResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{id}/update-tier")
    @Operation(summary = "Update customer tier", description = "Recalculate and update loyalty tier")
    public ResponseEntity<CustomerResponse> updateTier(@PathVariable Long id) {
        log.info("Updating tier for customer: {}", id);

        customerService.updateTierLevel(id);

        Customer customer = customerService.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        CustomerResponse response = customerMapper.toResponse(customer);
        return ResponseEntity.ok(response);
    }
}
