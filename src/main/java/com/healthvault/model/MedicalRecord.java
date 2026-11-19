package com.healthvault.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * MedicalRecord Entity - Stores health records linked to patients.
 *
 * Key Java Concepts:
 * - @ManyToOne: Multiple records can belong to one patient (Many-to-One relationship)
 * - Enum (RecordType): Type-safe constant for different types of medical records
 * - @PrePersist: Lifecycle callback to auto-set upload timestamp
 * - Cloud Integration: fileUrl stores the Supabase Storage URL of uploaded file
 */
@Entity
@Table(name = "medical_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many records can belong to one patient
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    // The doctor who added this record (optional)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Enumerated(EnumType.STRING)
    private RecordType recordType;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    // Original filename (e.g., "blood_test.pdf")
    private String fileName;

    // File type (e.g., "application/pdf", "image/jpeg")
    private String fileType;

    // URL of the file stored in Supabase Cloud Storage
    @Column(columnDefinition = "TEXT")
    private String fileUrl;

    // Path of the file in the cloud storage bucket
    private String cloudStoragePath;

    private LocalDateTime uploadedAt;

    // Type-safe enum for record categories
    public enum RecordType {
        LAB_REPORT,
        PRESCRIPTION,
        XRAY,
        SCAN,
        VACCINATION,
        DISCHARGE_SUMMARY,
        OTHER
    }

    @PrePersist
    protected void onCreate() {
        uploadedAt = LocalDateTime.now();
    }
}
