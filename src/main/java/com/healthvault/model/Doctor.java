package com.healthvault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Doctor Entity - Stores doctor-specific profile information.
 *
 * Key Java Concepts:
 * - @OneToOne: Each doctor is linked to one User account
 * - OOP - Separation of Concerns: Doctor details are separate from User auth details
 */
@Entity
@Table(name = "doctors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String specialization;

    private String licenseNumber;

    private String phone;

    private String hospitalName;

    private String qualifications;

    private int experienceYears;
}
