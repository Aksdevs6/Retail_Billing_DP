package com.textile.billing.controller;

import com.textile.billing.dto.BillRequest;
import com.textile.billing.entity.Bill;
import com.textile.billing.service.BillService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER: BillController
 * ----------------------------
 * Handles all billing-related HTTP requests.
 *
 * BASE URL: http://localhost:8080/api/bills
 *
 * ENDPOINTS:
 *   POST   /api/bills        → Create new bill
 *   GET    /api/bills        → Get all bills
 *   GET    /api/bills/{id}   → Get one bill by ID
 */
@RestController
@RequestMapping("/api/bills")
@CrossOrigin(origins = "*")
public class BillController {

    @Autowired
    private BillService billService;

    /**
     * POST /api/bills
     * Creates a new bill (main billing operation)
     *
     * REQUEST BODY example:
     * {
     *   "discountPercent": 10,
     *   "items": [
     *     { "productId": 1, "quantity": 2 },
     *     { "productId": 3, "quantity": 1 }
     *   ]
     * }
     *
     * WHAT HAPPENS INTERNALLY:
     *   1. BillRequest received and validated
     *   2. Service checks stock for each item
     *   3. Calculates totals + GST + discount
     *   4. Reduces product stock
     *   5. Saves bill + items to DB
     *   6. Returns created bill as JSON
     */
    @PostMapping
    public ResponseEntity<Bill> createBill(@Valid @RequestBody BillRequest request) {
        Bill createdBill = billService.createBill(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBill);  // 201 Created
    }

    /**
     * GET /api/bills
     * Returns all bills (bill history)
     */
    @GetMapping
    public ResponseEntity<List<Bill>> getAllBills() {
        List<Bill> bills = billService.getAllBills();
        return ResponseEntity.ok(bills);
    }

    /**
     * GET /api/bills/{id}
     * Returns one bill with all its items
     *
     * Example: GET http://localhost:8080/api/bills/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBillById(@PathVariable Long id) {
        Bill bill = billService.getBillById(id);
        return ResponseEntity.ok(bill);
    }
}
