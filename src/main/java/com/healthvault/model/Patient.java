package com.healthvault.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Patient Entity - Stores patient-specific profile information.
 *
 * Key Java Concepts:
 * - @OneToOne: One User can have one Patient profile (1:1 relationship)
 * - @JoinColumn: Specifies the foreign key column in the database
 * - Encapsulation: Private fields with Lombok-generated getters/setters
 */
@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-one relationship: each patient has one user account
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String dateOfBirth;

    private String bloodGroup;

    private String phone;

    private String address;

    private String emergencyContact;

    private String allergies;

    private String existingConditions;
}
