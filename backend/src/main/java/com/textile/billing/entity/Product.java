package com.textile.billing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

/**
 * ENTITY: Product
 * ----------------
 * Represents the 'product' table in MySQL.
 *
 * JPA Annotations:
 *   @Entity  → tells Hibernate this class maps to a DB table
 *   @Table   → specifies the exact table name
 *   @Id      → marks primary key field
 *   @GeneratedValue → auto-increment ID
 *   @Column  → optional: customize column name/constraints
 *
 * Lombok Annotations:
 *   @Data         → generates getters, setters, toString, equals
 *   @NoArgsConstructor → generates default constructor
 *   @AllArgsConstructor → generates constructor with all fields
 */
@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Validation: name must not be blank, max 100 chars
    @NotBlank(message = "Product name is required")
    @Size(max = 100, message = "Name must be under 100 characters")
    @Column(name = "name", nullable = false)
    private String name;

    // Validation: price must be at least 1.0
    @NotNull(message = "Price is required")
    @Min(value = 1, message = "Price must be at least 1")
    @Column(name = "price", nullable = false)
    private Double price;

    // Validation: quantity cannot be negative
    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // GST % for this product (optional enhancement)
    @Column(name = "gst_percent")
    private Double gstPercent = 5.0;
}
