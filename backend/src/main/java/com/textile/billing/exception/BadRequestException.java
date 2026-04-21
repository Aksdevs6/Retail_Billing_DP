package com.textile.billing.exception;

/**
 * CUSTOM EXCEPTION: BadRequestException
 * ---------------------------------------
 * Thrown when business rules are violated.
 * Results in HTTP 400 Bad Request response.
 *
 * Example usage:
 *   throw new BadRequestException("Insufficient stock for: Cotton Saree");
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
