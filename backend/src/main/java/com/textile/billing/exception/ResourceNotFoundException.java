package com.textile.billing.exception;

/**
 * CUSTOM EXCEPTION: ResourceNotFoundException
 * ---------------------------------------------
 * Thrown when a requested resource (product/bill) is not found in DB.
 * Results in HTTP 404 Not Found response.
 *
 * Example usage:
 *   throw new ResourceNotFoundException("Product not found with ID: " + id);
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
