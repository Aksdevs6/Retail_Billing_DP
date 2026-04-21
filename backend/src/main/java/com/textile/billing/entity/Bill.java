package com.textile.billing.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ENTITY: Bill
 * -------------
 * Represents one complete purchase transaction.
 * One Bill → many BillItems (One-to-Many relationship)
 *
 * DATABASE RELATIONSHIP:
 *   bill (1) ──── (*) bill_item
 *
 * @OneToMany: One Bill can have many BillItems
 * cascade = ALL   → saving/deleting a bill also saves/deletes its items
 * orphanRemoval   → if an item is removed from list, delete from DB too
 */
@Entity
@Table(name = "bill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Auto-set to current time when bill is created
    @Column(name = "bill_date", nullable = false)
    private LocalDateTime billDate;

    // Sum of all item prices (before GST and discount)
    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    // Total GST calculated across all items
    @Column(name = "gst_amount")
    private Double gstAmount = 0.0;

    // Discount applied on the bill
    @Column(name = "discount_amount")
    private Double discountAmount = 0.0;

    // Final amount = totalAmount + gstAmount - discountAmount
    @Column(name = "net_amount", nullable = false)
    private Double netAmount;

    /**
     * ONE-TO-MANY RELATIONSHIP
     * mappedBy = "bill" → refers to the 'bill' field in BillItem class
     * cascade = ALL     → operations on Bill cascade to BillItems
     */
    @OneToMany(mappedBy = "bill", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillItem> billItems = new ArrayList<>();

    // Called before persisting to DB - sets the date automatically
    @PrePersist
    public void setDate() {
        this.billDate = LocalDateTime.now();
    }
}
