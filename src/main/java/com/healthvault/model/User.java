package com.healthvault.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

/**
 * User Entity - Represents all users of the system (Patients, Doctors, Admins).
 *
 * Key Java Concepts:
 * - @Entity: Maps this class to a database table using JPA/Hibernate
 * - Implements UserDetails: Integrates with Spring Security for authentication
 * - Enum (Role): Type-safe constant values for user roles
 * - Inheritance: Patient and Doctor extend the concept of User
 */
@Entity
@Table(name = "users")
@Data                   // Lombok: auto-generates getters, setters, toString, equals, hashCode
@NoArgsConstructor      // Lombok: generates no-arg constructor (required by JPA)
@AllArgsConstructor     // Lombok: generates all-arg constructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String fullName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Enum for type-safe role management
    public enum Role {
        PATIENT, DOCTOR, ADMIN
    }

    // Lifecycle callbacks - automatically set timestamps
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // ===== Spring Security UserDetails Methods =====

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Returns the user's role as a Spring Security authority
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getUsername() {
        return email; // Email is used as the username
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}
