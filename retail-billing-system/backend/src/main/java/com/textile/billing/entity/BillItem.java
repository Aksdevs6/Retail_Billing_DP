package com.textile.billing.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * ENTITY: BillItem
 * -----------------
 * Represents ONE line item within a bill.
 * (e.g., "2 x Cotton Saree @ ₹850 = ₹1700")
 *
 * DATABASE RELATIONSHIPS:
 *   bill_item (*) ──── (1) bill      → Many items belong to one bill
 *   bill_item (*) ──── (1) product   → Each item references one product
 *
 * @ManyToOne: Many BillItems belong to one Bill/Product
 * @JoinColumn: specifies the foreign key column name in DB
 */
@Entity
@Table(name = "bill_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * MANY-TO-ONE: Many BillItems → One Bill
     * @JoinColumn → creates 'bill_id' column as foreign key
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    private Bill bill;

    /**
     * MANY-TO-ONE: Many BillItems → One Product
     * @JoinColumn → creates 'product_id' column as foreign key
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // How many units of this product were purchased
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    // Product price AT THE TIME OF PURCHASE (snapshot)
    // This is important: even if product price changes later,
    // the bill still shows the price at the time of purchase
    @Column(name = "unit_price", nullable = false)
    private Double unitPrice;

    // quantity × unitPrice
    @Column(name = "total_price", nullable = false)
    private Double totalPrice;
}
