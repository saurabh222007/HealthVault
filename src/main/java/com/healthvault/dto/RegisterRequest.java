package com.healthvault.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * RegisterRequest DTO (Data Transfer Object)
 *
 * Key Java Concepts:
 * - DTO Pattern: Separates the data sent over HTTP from the actual database Entity.
 *   We never expose the entity directly to the web layer.
 * - Bean Validation annotations: @NotBlank, @Email, @Size enforce input rules
 *   automatically before the data reaches the service layer.
 */
@Data
public class RegisterRequest {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Please confirm your password")
    private String confirmPassword;

    @NotBlank(message = "Please select a role")
    private String role; // "PATIENT" or "DOCTOR"
}
