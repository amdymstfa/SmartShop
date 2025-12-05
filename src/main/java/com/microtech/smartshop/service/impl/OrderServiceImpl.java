package com.microtech.smartshop.service.impl;

import com.microtech.smartshop.dto.request.CreateOrderRequest;
import com.microtech.smartshop.dto.request.OrderItemRequest;
import com.microtech.smartshop.entity.*;
import com.microtech.smartshop.enums.OrderStatus;
import com.microtech.smartshop.exception.BusinessRuleException;
import com.microtech.smartshop.exception.InsufficientStockException;
import com.microtech.smartshop.exception.ResourceNotFoundException;
import com.microtech.smartshop.repository.*;
import com.microtech.smartshop.service.OrderService;
import com.microtech.smartshop.service.generic.GenericServiceImpl;
import com.microtech.smartshop.util.LoyaltyDiscountCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Implementation of the Order service.
 * This is the most complex service: handles calculations, stock management, discounts, promotions, and business rules.
 */
@Slf4j
@Service
@Transactional
public class OrderServiceImpl
        extends GenericServiceImpl<Order, Long, OrderRepository>
        implements OrderService {

    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final LoyaltyDiscountCalculator discountCalculator;

    @Value("${app.tax.vat-rate:0.20}")
    private BigDecimal vatRate;

    public OrderServiceImpl(
            OrderRepository repository,
            CustomerRepository customerRepository,
            ProductRepository productRepository,
            PromoCodeRepository promoCodeRepository,
            LoyaltyDiscountCalculator discountCalculator) {
        super(repository);
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.promoCodeRepository = promoCodeRepository;
        this.discountCalculator = discountCalculator;
    }

    @Override
    protected String getEntityName() {
        return "Order";
    }

    // ============================================
    // ORDER CREATION
    // ============================================

    @Override
    public Order createOrder(CreateOrderRequest request) {
        log.info("Creating order for customer {}", request.getCustomerId());

        // 1. Retrieve the customer
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer with ID " + request.getCustomerId() + " not found"));

        if (customer.getIsDeleted()) {
            throw new BusinessRuleException("Cannot create an order for a deleted customer");
        }

        // 2. Create the order
        Order order = Order.builder()
                .customer(customer)
                .status(OrderStatus.PENDING)
                .build();

        // 3. Add items and check stock
        for (OrderItemRequest itemReq : request.getItems()) {
            Product product = productRepository.findByIdAndIsDeletedFalse(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product with ID " + itemReq.getProductId() + " not found"));

            // Check product stock
            if (!product.hasStock(itemReq.getQuantity())) {
                order.reject();
                repository.save(order);
                throw new InsufficientStockException(
                        String.format("Insufficient stock for product '%s'. Available: %d, Requested: %d",
                                product.getName(), product.getStock(), itemReq.getQuantity()));
            }

            // Create the order item
            OrderItem item = OrderItem.builder()
                    .product(product)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(product.getUnitPrice())
                    .build();
            item.calculateSubTotal();
            order.addItem(item);

            // Decrease product stock
            product.decrementStock(itemReq.getQuantity());
            productRepository.save(product);
        }

        // 4. Calculate initial totals
        order.calculateTotals(vatRate);

        // 5. Apply loyalty discount
        BigDecimal loyaltyDiscount = discountCalculator.calculateDiscount(
                customer.getTier(),
                order.getSubtotalExcludingTax()
        );
        if (loyaltyDiscount.compareTo(BigDecimal.ZERO) > 0) {
            order.setDiscountAmount(loyaltyDiscount);
            log.info("Applied loyalty discount: {} (tier: {})", loyaltyDiscount, customer.getTier());
        }

        // 6. Apply promo code if present
        if (request.getPromoCode() != null && !request.getPromoCode().isBlank()) {
            PromoCode promoCode = promoCodeRepository.findValidPromoCodeByCode(
                    request.getPromoCode(),
                    java.time.LocalDate.now()
            ).orElseThrow(() -> new BusinessRuleException(
                    "Invalid or expired promo code: " + request.getPromoCode()));

            BigDecimal promoDiscount = order.getSubtotalExcludingTax()
                    .multiply(promoCode.getDiscountPercentage())
                    .divide(BigDecimal.valueOf(100), 2, BigDecimal.ROUND_HALF_UP);

            order.setDiscountAmount(order.getDiscountAmount().add(promoDiscount));
            order.setPromoCode(promoCode.getCode());

            // Mark promo code as used
            promoCode.markAsUsed();
            promoCodeRepository.save(promoCode);

            log.info("Applied promo code {} discount: {}", promoCode.getCode(), promoDiscount);
        }

        // 7. Recalculate totals after discounts
        order.calculateTotals(vatRate);

        // 8. Save order
        Order savedOrder = repository.save(order);
        log.info("Order {} created successfully. Total: {}", savedOrder.getId(), savedOrder.getTotalIncludingTax());

        return savedOrder;
    }

    // ============================================
    // ORDER STATUS MANAGEMENT
    // ============================================

    @Override
    public Order confirmOrder(Long id) {
        Order order = findByIdOrThrow(id);

        if (!order.canBeConfirmed()) {
            throw new BusinessRuleException(
                    "Order cannot be confirmed. Status: " + order.getStatus() +
                            ", Fully paid: " + order.isFullyPaid());
        }

        order.confirm();

        // Update customer statistics
        Customer customer = order.getCustomer();
        customer.updateStatistics(order.getTotalIncludingTax());
        customerRepository.save(customer);

        repository.save(order);
        log.info("Order {} confirmed. Customer {} statistics updated", id, customer.getId());

        return order;
    }

    @Override
    public Order cancelOrder(Long id) {
        Order order = findByIdOrThrow(id);

        if (!order.getStatus().equals(OrderStatus.PENDING)) {
            throw new BusinessRuleException("Only PENDING orders can be canceled");
        }

        // Restore stock
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.incrementStock(item.getQuantity());
            productRepository.save(product);
        }

        // Release promo code if used
        if (order.getPromoCode() != null) {
            promoCodeRepository.findByCode(order.getPromoCode())
                    .ifPresent(promo -> {
                        promo.setUsed(false);
                        promoCodeRepository.save(promo);
                    });
        }

        order.cancel();
        repository.save(order);
        log.info("Order {} canceled. Stock restored", id);

        return order;
    }

    @Override
    public Order rejectOrder(Long id) {
        Order order = findByIdOrThrow(id);
        order.reject();
        repository.save(order);
        log.info("Order {} rejected", id);
        return order;
    }

    // ============================================
    // QUERY METHODS
    // ============================================

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findByCustomerId(Long customerId, Pageable pageable) {
        return repository.findByCustomerId(customerId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Order> findByStatus(OrderStatus status, Pageable pageable) {
        return repository.findByStatus(status, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Order findByIdWithDetails(Long id) {
        return repository.findByIdWithItemsAndPayments(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Order with ID " + id + " not found"));
    }
}
