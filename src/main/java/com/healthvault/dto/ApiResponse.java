package com.healthvault.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ApiResponse DTO - Standard JSON response wrapper for REST API calls.
 *
 * Key Java Concepts:
 * - Generic class using type parameter <T> for flexible response data types
 * - Encapsulation: wraps success/error info in a consistent structure
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    // Static factory methods for clean API responses
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
