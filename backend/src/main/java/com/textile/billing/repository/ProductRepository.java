package com.textile.billing.repository;

import com.textile.billing.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * REPOSITORY: ProductRepository
 * --------------------------------
 * This interface handles all database operations for Product.
 *
 * HOW IT WORKS:
 *   - Extends JpaRepository<Product, Long>
 *     → Product = entity class
 *     → Long    = type of primary key (id)
 *
 *   - JpaRepository gives us FREE methods (no SQL needed!):
 *     → save(product)          = INSERT or UPDATE
 *     → findById(id)           = SELECT WHERE id = ?
 *     → findAll()              = SELECT * FROM product
 *     → deleteById(id)         = DELETE WHERE id = ?
 *     → existsById(id)         = check if record exists
 *
 *   - We can also add CUSTOM methods using Spring's naming convention:
 *     → findByName()           = SELECT WHERE name = ?
 *     → findByQuantityLessThan() = SELECT WHERE quantity < ?
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Custom method: find products by name (case-insensitive search)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Custom method: find products with stock below a threshold (low stock alert)
    List<Product> findByQuantityLessThan(Integer threshold);
}
