package com.microtech.smartshop.repository;

import com.microtech.smartshop.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByEmail(String email);

    List<Customer> findByIsDeletedFalse(Pageable pageable);

    Optional<Customer> findByIdAndIsDeletedFalse(Long id);

    boolean existsByEmail(String email);
}
