package com.textile.billing.service;

import com.textile.billing.entity.Product;
import com.textile.billing.exception.BadRequestException;
import com.textile.billing.exception.ResourceNotFoundException;
import com.textile.billing.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SERVICE: ProductService
 * ------------------------
 * The SERVICE LAYER handles all business logic.
 *
 * RESPONSIBILITIES:
 *   - Validate business rules (beyond basic @Valid annotations)
 *   - Call repository to interact with DB
 *   - Transform data if needed
 *   - NOT responsible for HTTP (that's the controller's job)
 *
 * @Service → marks this as a Spring-managed service bean
 *           → Spring creates one instance and injects it wherever needed
 *
 * FLOW: Controller → Service → Repository → DB
 */
@SuppressWarnings("unused")
@Service
public class ProductService {

    // @Autowired injects the ProductRepository automatically (Dependency Injection)
    @Autowired
    private ProductRepository productRepository;

    /**
     * GET ALL PRODUCTS
     * Returns the complete list of products from DB
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * GET PRODUCT BY ID
     * Returns a single product or throws 404 if not found
     */
    public Product getProductById(Long id) {
        // orElseThrow → if Optional is empty, throw exception
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Product not found with ID: " + id));
    }

    /**
     * ADD NEW PRODUCT
     * Saves a new product to the database
     */
    public Product addProduct(Product product) {
        // Business rule: Price and quantity must be valid (already validated by @Valid)
        // save() → performs INSERT into product table
        return productRepository.save(product);
    }

    /**
     * UPDATE EXISTING PRODUCT
     * Finds the product, updates its fields, saves back to DB
     */
    public Product updateProduct(Long id, Product updatedProduct) {
        // Step 1: Find existing product (throws 404 if not found)
        Product existing = getProductById(id);

        // Step 2: Update only the fields that are allowed to change
        existing.setName(updatedProduct.getName());
        existing.setPrice(updatedProduct.getPrice());
        existing.setQuantity(updatedProduct.getQuantity());
        existing.setGstPercent(updatedProduct.getGstPercent());

        // Step 3: Save updated product → performs UPDATE query
        return productRepository.save(existing);
    }

    /**
     * DELETE PRODUCT
     * Removes a product from the database
     */
    public void deleteProduct(Long id) {
        // Check if product exists first
        Product product = getProductById(id);

        // Business rule: Don't delete if product is referenced in any bills
        // (This is handled by FK constraint in DB - will throw DataIntegrityViolationException)
        productRepository.delete(product);
    }

    /**
     * SEARCH PRODUCTS BY NAME
     * Useful for search bar in frontend
     */
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * GET LOW STOCK PRODUCTS
     * Returns products with quantity below given threshold
     * Useful for inventory alerts
     */
    public List<Product> getLowStockProducts(Integer threshold) {
        return productRepository.findByQuantityLessThan(threshold);
    }
}
