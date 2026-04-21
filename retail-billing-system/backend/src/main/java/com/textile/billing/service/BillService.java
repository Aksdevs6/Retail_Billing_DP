package com.textile.billing.service;

import com.textile.billing.dto.BillRequest;
import com.textile.billing.entity.*;
import com.textile.billing.exception.BadRequestException;
import com.textile.billing.exception.ResourceNotFoundException;
import com.textile.billing.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * SERVICE: BillService
 * ---------------------
 * Contains the CORE BILLING LOGIC.
 *
 * KEY CONCEPTS:
 *
 * @Transactional:
 *   → Wraps the entire method in a DB transaction
 *   → If ANY step fails (e.g., stock update), ALL changes are rolled back
 *   → Ensures data consistency (no partial bills saved)
 *
 * BILLING LOGIC FLOW:
 *   1. Receive BillRequest (list of productId + quantity pairs)
 *   2. For each item:
 *      a. Find the product in DB
 *      b. Check if enough stock is available
 *      c. Calculate item total (qty × price)
 *      d. Calculate GST for this item
 *      e. Reduce product stock
 *   3. Calculate bill totals (subtotal, GST, discount, net)
 *   4. Save Bill + BillItems to DB
 *   5. Return saved Bill
 */
@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * CREATE BILL - Main billing method
     *
     * @param request - Contains list of items and discount percent
     * @return Saved Bill entity
     */
    @Transactional  // All DB operations here succeed together or fail together
    public Bill createBill(BillRequest request) {

        // ── STEP 1: Create empty Bill object ──────────────────────
        Bill bill = new Bill();
        List<BillItem> billItems = new ArrayList<>();

        double totalAmount = 0.0;   // Sum of all item prices (before GST)
        double totalGst    = 0.0;   // Total GST across all items

        // ── STEP 2: Process each item in the request ──────────────
        for (BillRequest.BillItemRequest itemReq : request.getItems()) {

            // 2a. Find the product in DB
            Product product = productRepository.findById(itemReq.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Product not found with ID: " + itemReq.getProductId()));

            // 2b. STOCK CHECK: Is enough quantity available?
            if (product.getQuantity() < itemReq.getQuantity()) {
                throw new BadRequestException(
                        "Insufficient stock for '" + product.getName() +
                        "'. Available: " + product.getQuantity() +
                        ", Requested: " + itemReq.getQuantity());
            }

            // 2c. Calculate item-level totals
            double unitPrice   = product.getPrice();
            double itemTotal   = unitPrice * itemReq.getQuantity();
            double gstPercent  = product.getGstPercent() != null ? product.getGstPercent() : 5.0;
            double itemGst     = (itemTotal * gstPercent) / 100;  // GST = amount × rate / 100

            // 2d. Add to running totals
            totalAmount += itemTotal;
            totalGst    += itemGst;

            // 2e. REDUCE STOCK: Decrease product quantity in DB
            product.setQuantity(product.getQuantity() - itemReq.getQuantity());
            productRepository.save(product);  // Save updated stock

            // 2f. Create BillItem entity (one line in the bill)
            BillItem billItem = new BillItem();
            billItem.setBill(bill);                      // Link to parent bill
            billItem.setProduct(product);                // Link to product
            billItem.setQuantity(itemReq.getQuantity()); // Qty purchased
            billItem.setUnitPrice(unitPrice);            // Price snapshot
            billItem.setTotalPrice(itemTotal);           // Line total
            billItems.add(billItem);
        }

        // ── STEP 3: Calculate discount ────────────────────────────
        double discountPercent = request.getDiscountPercent() != null
                ? request.getDiscountPercent() : 0.0;

        // Discount is applied on the base total (before GST)
        double discountAmount = (totalAmount * discountPercent) / 100;

        // Net Amount = Base Total + GST - Discount
        double netAmount = totalAmount + totalGst - discountAmount;

        // ── STEP 4: Set bill fields ───────────────────────────────
        bill.setTotalAmount(totalAmount);
        bill.setGstAmount(totalGst);
        bill.setDiscountAmount(discountAmount);
        bill.setNetAmount(netAmount);
        bill.setBillItems(billItems);

        // ── STEP 5: Save Bill to DB ───────────────────────────────
        // CascadeType.ALL → saving Bill also saves all BillItems automatically
        return billRepository.save(bill);
    }

    /**
     * GET ALL BILLS
     * Returns all bills (for bill history page)
     */
    public List<Bill> getAllBills() {
        return billRepository.findAll();
    }

    /**
     * GET BILL BY ID
     * Returns a specific bill with all its items
     */
    public Bill getBillById(Long id) {
        return billRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Bill not found with ID: " + id));
    }
}
