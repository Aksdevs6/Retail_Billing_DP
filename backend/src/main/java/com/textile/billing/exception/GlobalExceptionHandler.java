package com.textile.billing.exception;

import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * EXCEPTION HANDLER
 * ------------------
 * Centralized error handling for the entire application.
 *
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody
 * → Intercepts exceptions thrown from any controller
 * → Returns JSON error responses instead of HTML error pages
 *
 * HOW IT WORKS:
 *   When an exception is thrown anywhere in the app:
 *   1. Spring looks for a matching @ExceptionHandler method here
 *   2. That method runs and returns a proper JSON error response
 *   3. The frontend receives a clean error message
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles: Resource not found (404)
     * Example: GET /api/products/999 when product 999 doesn't exist
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Handles: Business logic violations (400)
     * Example: Trying to bill more quantity than available stock
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Handles: Validation failures from @Valid annotation (400)
     * Example: Sending a product with empty name or negative price
     *
     * Returns a map of field → error message
     * e.g., { "name": "Product name is required", "price": "Price must be at least 1" }
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        // Loop through each field error and collect messages
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    /**
     * Handles: Any unexpected server error (500)
     * Acts as a catch-all for unhandled exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Something went wrong: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
