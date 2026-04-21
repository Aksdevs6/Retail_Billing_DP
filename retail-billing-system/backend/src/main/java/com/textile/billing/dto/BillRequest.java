package com.textile.billing.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

/**
 * DTO: BillRequest
 * -----------------
 * DTO = Data Transfer Object
 * Used to receive bill creation data from the frontend.
 *
 * WHY USE DTOs instead of Entity directly?
 *   - Frontend may send partial data (not all entity fields)
 *   - We don't want to expose internal entity fields to the client
 *   - Cleaner separation between API layer and database layer
 *
 * FLOW:
 *   Frontend sends JSON → BillRequest DTO → Service processes → Bill Entity → DB
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillRequest {

    // Optional discount percentage (0 to 100)
    @Min(value = 0, message = "Discount cannot be negative")
    @Max(value = 100, message = "Discount cannot exceed 100%")
    private Double discountPercent = 0.0;

    // List of items being purchased in this bill
    @NotNull(message = "Bill items cannot be null")
    @Size(min = 1, message = "At least one item is required")
    private List<BillItemRequest> items;

    /**
     * INNER DTO: BillItemRequest
     * Each item in the bill has a product ID and quantity
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BillItemRequest {

        @NotNull(message = "Product ID is required")
        private Long productId;

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        private Integer quantity;
    }
}
