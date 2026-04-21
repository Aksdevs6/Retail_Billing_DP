package com.textile.billing.controller;

import com.textile.billing.entity.Product;
import com.textile.billing.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CONTROLLER: ProductController
 * --------------------------------
 * The CONTROLLER LAYER handles HTTP requests and responses.
 *
 * RESPONSIBILITIES:
 *   - Define API endpoints (URLs)
 *   - Receive HTTP requests (GET, POST, PUT, DELETE)
 *   - Call Service layer for business logic
 *   - Return HTTP responses (JSON data + status codes)
 *
 * KEY ANNOTATIONS:
 *   @RestController  = @Controller + @ResponseBody
 *                    → All methods return JSON automatically
 *   @RequestMapping  → Sets base URL for all methods in this class
 *   @CrossOrigin     → Allows frontend (HTML) to call this API
 *                    → Without this, browsers block cross-origin requests
 *
 * BASE URL: http://localhost:8080/api/products
 *
 * STANDARD HTTP STATUS CODES USED:
 *   200 OK           → Successful GET / PUT
 *   201 Created      → Successful POST (resource created)
 *   204 No Content   → Successful DELETE
 *   400 Bad Request  → Validation failure
 *   404 Not Found    → Resource doesn't exist
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")  // Allow requests from any origin (frontend HTML)
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * GET /api/products
     * Returns all products
     *
     * Example: GET http://localhost:8080/api/products
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);  // 200 OK
    }

    /**
     * GET /api/products/{id}
     * Returns one product by ID
     *
     * @PathVariable → extracts {id} from the URL
     * Example: GET http://localhost:8080/api/products/1
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * POST /api/products
     * Creates a new product
     *
     * @RequestBody → reads the JSON body and maps it to Product object
     * @Valid       → triggers validation annotations on Product entity
     * Example: POST http://localhost:8080/api/products
     *   Body: { "name": "Cotton Saree", "price": 850, "quantity": 50 }
     */
    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        Product saved = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);  // 201 Created
    }

    /**
     * PUT /api/products/{id}
     * Updates an existing product
     *
     * Example: PUT http://localhost:8080/api/products/1
     *   Body: { "name": "Cotton Saree Updated", "price": 900, "quantity": 45 }
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE /api/products/{id}
     * Deletes a product by ID
     *
     * Example: DELETE http://localhost:8080/api/products/1
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();  // 204 No Content
    }

    /**
     * GET /api/products/search?name=cotton
     * Search products by name
     *
     * @RequestParam → reads ?name=... from URL query string
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        List<Product> results = productService.searchByName(name);
        return ResponseEntity.ok(results);
    }

    /**
     * GET /api/products/low-stock?threshold=10
     * Get products with stock below threshold
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStock(
            @RequestParam(defaultValue = "10") Integer threshold) {
        List<Product> results = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(results);
    }
}
