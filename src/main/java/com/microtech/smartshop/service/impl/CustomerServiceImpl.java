package com.microtech.smartshop.service.impl ;

import com.microtech.smartshop.dto.response.CustomerStatistics;
import com.microtech.smartshop.entity.Customer;
import com.microtech.smartshop.entity.Order;
import com.microtech.smartshop.repository.CustomerRepository;
import com.microtech.smartshop.repository.OrderRepository;
import com.microtech.smartshop.service.CustomerService;
import com.microtech.smartshop.service.generic.GenericServiceImpl;
import com.microtech.smartshop.util.CustomerTierCalculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.microtech.smartshop.exception.ResourceNotFoundException;

import java.util.List;

@Slf4j
@Service
@Transactional
public class CustomerServiceImpl
        extends GenericServiceImpl<Customer, Long, CustomerRepository>
        implements CustomerService {

    private final OrderRepository orderRepository ;
    private final CustomerTierCalculator tierCalculator ;

    public CustomerServiceImpl(
            CustomerRepository repository,
            OrderRepository orderRepository,
            CustomerTierCalculator tierCalculator) {
        super(repository);
        this.orderRepository = orderRepository;
        this.tierCalculator = tierCalculator;
    }

    @Override
    public CustomerStatistics getCustomerStatistics(Long id) {

        Customer customer = findByIdOrThrow(id);

        return CustomerStatistics.builder()
                .totalOrders(customer.getTotalOrders())
                .totalSpent(customer.getTotalSpent())
                .currentTier(customer.getTier())
                .firstOrderDate(customer.getFirstOrderDate())
                .lastOrderDate(customer.getLastOrderDate())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getOrderHistory(Long id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Customer with ID " + id + " not found");
        }
        return orderRepository.findByCustomerId(id, org.springframework.data.domain.Pageable.unpaged()).getContent();
    }

    @Override
    public void updateTierLevel(Long id) {
        Customer customer = findByIdOrThrow(id) ;

        var newTier = tierCalculator.calculateTier(
                customer.getTotalOrders(),
                customer.getTotalSpent()
        );

        if (!newTier.equals(customer.getTier())){
            log.info(
                    "Updating customer {} tier : {} -> {}", id , customer.getTier(), newTier
            );
            customer.setTier(newTier);
            repository.save(customer);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Customer findByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Customer with email " + email + " not found")
                );
    }

    @Override
    public void softDelete(Long id) {
        Customer customer = findByIdOrThrow(id);
        customer.softDelete();
        repository.save(customer);
        log.info("Customer {} soft deleted", id);
    }

    @Override
    protected String getEntityName() {
        return "Customer";
    }
}