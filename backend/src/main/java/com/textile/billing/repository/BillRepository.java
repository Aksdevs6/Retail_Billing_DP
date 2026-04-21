package com.textile.billing.repository;

import com.textile.billing.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REPOSITORY: BillRepository
 * ----------------------------
 * Handles all database operations for Bill.
 * Inherits all CRUD methods from JpaRepository.
 */
@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    // Custom: find bills created between two dates (for reports)
    List<Bill> findByBillDateBetween(LocalDateTime start, LocalDateTime end);

    // Custom: find bills whose net amount is above a given value
    List<Bill> findByNetAmountGreaterThan(Double amount);
}
